package com.scm.services;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scm.entities.Document;
import com.scm.entities.User;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.DocumentRepository;

@Service
public class DocumentService {

	@Autowired
	S3Service s3Service;

	@Autowired
	DocumentRepository documentRepository;

	@Autowired
	PdfThumbnailService pdfThumbnailService;
	
	@Autowired
	CalibrePdfToEpubService calibrePdfToEpubService;

	public Document saveDocument(MultipartFile file, User user) throws IOException {
		
		String key = s3Service.uploadFile(file);

		String thumbnailKey = null;
		if ("application/pdf".equalsIgnoreCase(file.getContentType())) {
			byte[] thumbnail = pdfThumbnailService.generateThumbnail(file);
			thumbnailKey = s3Service.uploadThumbnail(thumbnail, file.getOriginalFilename());
		}

		return documentRepository.save(
				Document.builder().fileKey(key).fileName(file.getOriginalFilename()).contentType(file.getContentType())
						.user(user).sizeInBytes(file.getSize()).thumbnailKey(thumbnailKey).build());
	}
//	save Pfd to epub file
	public Document saveEpubDocument(MultipartFile file , User user) throws IOException, InterruptedException{
		
		File epubFile = calibrePdfToEpubService.convertPdfToEpub(file);
		
		String epubKey = s3Service.uploadFile(epubFile);
		
//		String thumbnailKey = null;
//		if ("application/pdf".equalsIgnoreCase(file.getContentType())) {
//			byte[] thumbnail = pdfThumbnailService.generateThumbnail(file);
//			thumbnailKey = s3Service.uploadThumbnail(thumbnail, file.getOriginalFilename());
//		}
		
		epubFile.delete();
		
		return documentRepository.save(
				Document.builder()
				.fileKey(epubKey)
				.fileName(file.getOriginalFilename())
				.contentType("application/epub+zip")
				.user(user)
				.sizeInBytes(file.getSize())
				.build());
	}

	public byte[] downloadDocument(Long id) {
		Document document = documentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("File not found"));

		return s3Service.downloadFile(document.getFileKey());
	}

	public List<Document> getDocumentByUser(User user) {
		return documentRepository.findByUser(user);
	}

	public Document getDocumenyById(Long id) {
		return documentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Document not found"));
	}

	public boolean deleteDocumentbyId(Long id) {
		try {
			Document document = getDocumenyById(id);
			s3Service.deleteFile(document.getFileKey());
			s3Service.deleteFile(document.getThumbnailKey());
			documentRepository.delete(document);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

}
