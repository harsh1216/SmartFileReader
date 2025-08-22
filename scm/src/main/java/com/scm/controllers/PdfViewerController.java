package com.scm.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.dtos.PdfAnnotationDTO;
import com.scm.entities.Document;
import com.scm.entities.PdfAnnotation;
import com.scm.services.DocumentService;
import com.scm.services.PdfAnnotationService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/document/{docId}/annotations")
@RequiredArgsConstructor
public class PdfViewerController {

	
	@Autowired
	PdfAnnotationService pdfAnnotationService;

	@Autowired
	DocumentService documentService;

 
	
//	@Autowired
//	S3Service s3Service;
	
//	@GetMapping("/{id}/view")
//	public String view(@PathVariable Long id,Model model,Authentication authentication,@ModelAttribute("user") User user) {
//		Document document = documentService.getDocumenyById(id);
//		
//		model.addAttribute("docId", id);
//		model.addAttribute("fileName", document.getFileName());
//		
//		return "document/viewer";
//		
//	}
	
	@GetMapping
	public ResponseEntity<List<PdfAnnotation>> getAnnotation(@PathVariable Long docId){
		Document document = documentService.getDocumenyById(docId);
		
		List<PdfAnnotation> annotations = pdfAnnotationService.getPdfAnnotationByDocument(document);
		return ResponseEntity.ok(annotations);
	}
	
	@PostMapping
	public ResponseEntity<PdfAnnotation> addAnnotation(@PathVariable Long docId,@RequestBody PdfAnnotationDTO annotationDTO){
		Document document = documentService.getDocumenyById(docId);
		PdfAnnotation saved = pdfAnnotationService.cretePdfAnnotation(document, annotationDTO);
		
		return ResponseEntity.ok(saved);
		
	}
	
	@DeleteMapping("/{pdfAnnotationId}")
	public ResponseEntity<Void> deletePdfAnnotation(@PathVariable Long docId, @PathVariable Long pdfAnnotationId){
		pdfAnnotationService.deletePdfAnnotationById(pdfAnnotationId);
		
		return ResponseEntity.noContent().build();
	}
	
	
}
