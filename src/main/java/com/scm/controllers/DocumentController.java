package com.scm.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.scm.entities.Document;
import com.scm.entities.User;
import com.scm.services.DocumentService;
import com.scm.services.S3Service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/document")
public class DocumentController {

	@Autowired
	DocumentService documentService;
	
	@Autowired
	S3Service s3Service;

	@PostMapping("/upload")
	public String uploadDocument(@RequestParam("file") MultipartFile file, @ModelAttribute("user") User user)
			throws IOException {
		System.out.println("user to add in document is: " + user.getName());
//		try {
//			documentService.saveEpubDocument(file, user);
//		} catch (IOException | InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		documentService.saveDocument(file, user);

		return "redirect:/user/fileManager";
	}

	@GetMapping("/{id}/download")
	public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {

		Document doc = documentService.getDocumenyById(id);

		byte[] data = documentService.downloadDocument(id);

		MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

		if (doc.getContentType() != null) {
			try {
				mediaType = MediaType.parseMediaType(doc.getContentType());
			} catch (IllegalArgumentException ignore) {

			}
		}

		String filename = (doc.getFileName() == null || doc.getFileName().isBlank()) ? "download"
				: doc.getFileName().replace("\"", "");

		String contentDisposition = "attachment; filename=\"" + filename + "\"; filename*=UTF-8''"
				+ java.net.URLEncoder.encode(filename, java.nio.charset.StandardCharsets.UTF_8);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).contentType(mediaType)
				.contentLength(data.length).body(data);
	}
	
	@GetMapping("/{id}/delete")
	public String deleteDocument(@PathVariable Long id, Model model) {
		
		boolean isDeleted = documentService.deleteDocumentbyId(id);
		
		
		model.addAttribute("isDeleted", isDeleted);
		
		return "redirect:/user/fileManager";
		
	}
	
	 @GetMapping("/thumbnails/{documentId}")
	    public ResponseEntity<byte[]> getThumbnail(@PathVariable Long documentId) {
	        Document doc = documentService.getDocumenyById(documentId);
	                
	        byte[] thumbnailBytes = null;
	        
	        if (doc.getThumbnailKey() == null) {
	        	thumbnailBytes = s3Service.downloadFile(doc.getFileKey());
	        }else {

	        thumbnailBytes = s3Service.downloadFile(doc.getThumbnailKey());
	        }

	        return ResponseEntity.ok()
	                .header("Content-Type", "image/png")
	                .body(thumbnailBytes);
	    }
	    
	    @GetMapping("/{id}/view")
	    public ResponseEntity<byte[]> viewDocument(@PathVariable Long id) {
	        Document document = documentService.getDocumenyById(id);

	        byte[] fileBytes = documentService.downloadDocument(id);
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.parseMediaType(document.getContentType()));
	        headers.setContentDisposition(ContentDisposition.inline().filename(document.getFileName()).build());

	        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
	    }

	    
	    
	

}
