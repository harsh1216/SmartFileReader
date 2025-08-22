package com.scm.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.dtos.PdfAnnotationDTO;
import com.scm.entities.Document;
import com.scm.entities.PdfAnnotation;
import com.scm.services.DocumentService;
import com.scm.services.PdfAnnotationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/annotation")
@RequiredArgsConstructor
public class PdfAnnotationController {

	@Autowired
	PdfAnnotationService pdfAnnotationService;
	
	@Autowired
	DocumentService documentService;
	
	@PostMapping("/{documentId}")
	public ResponseEntity<PdfAnnotation> createAnnotation(@PathVariable Long documentId,@RequestBody PdfAnnotationDTO annotationDTO ){
		Document document = documentService.getDocumenyById(documentId);
		
		PdfAnnotation pdfAnnotation = pdfAnnotationService.cretePdfAnnotation(document, annotationDTO);
		
		return ResponseEntity.ok(pdfAnnotation);
		
	}
	
	@GetMapping("/{documentId}")
	public ResponseEntity<List<PdfAnnotation>> getAnnotation(@PathVariable Long documentId){
		Document document = documentService.getDocumenyById(documentId);
		
		return ResponseEntity.ok(pdfAnnotationService.getPdfAnnotationByDocument(document));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAnnotation(@PathVariable Long id){
		pdfAnnotationService.deletePdfAnnotationById(id);
		return ResponseEntity.noContent().build();
	}
	
}
