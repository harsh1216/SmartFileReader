package com.scm.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scm.dtos.PdfAnnotationDTO;
import com.scm.entities.Document;
import com.scm.entities.PdfAnnotation;
import com.scm.repositories.PdfAnnotationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PdfAnnotationService {


	@Autowired
	PdfAnnotationRepository pdfAnnotationRepository;



	public List<PdfAnnotation> getPdfAnnotationByDocument(Document document) {
		try {
			return pdfAnnotationRepository.findByDocument(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public PdfAnnotation cretePdfAnnotation(Document document,PdfAnnotationDTO annotationDTO) {
		PdfAnnotation pdfAnnotation = PdfAnnotation.builder()
				.type(annotationDTO.getType())
				.pageNumber(annotationDTO.getPageName())
				.x(annotationDTO.getX())
				.y(annotationDTO.getY())
				.width(annotationDTO.getWidth())
				.height(annotationDTO.getHeight())
				.text(annotationDTO.getText())
				.color(annotationDTO.getColor())
				.document(document)
				.build();
		
		return pdfAnnotationRepository.save(pdfAnnotation);
	}
	
	public boolean deletePdfAnnotationById(Long id) {
		try {
			pdfAnnotationRepository.deleteById(id);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
