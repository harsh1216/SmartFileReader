package com.scm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.entities.Document;
import com.scm.entities.PdfAnnotation;
import java.util.List;


@Repository
public interface PdfAnnotationRepository extends JpaRepository<PdfAnnotation, Long> {
	List<PdfAnnotation> findByDocument(Document document);
}
