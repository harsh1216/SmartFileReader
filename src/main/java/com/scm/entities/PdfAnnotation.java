package com.scm.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PdfAnnotation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private PdfAnnotationType type;

	@Column(nullable = false)
	private int pageNumber;

	// Normalized coordinates (0..1). For bookmark you can leave nulls.
	private Float x;
	private Float y;
	private Float width;
	private Float height;
	
    // Optional: raw selected text or note text
	@Lob
	private String text;
	
    // Optional: hex color for highlight
    private String color;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;
    
    @LastModifiedDate
    private LocalDateTime updatedOn;
    
    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "document_id")
    private Document document;

}
