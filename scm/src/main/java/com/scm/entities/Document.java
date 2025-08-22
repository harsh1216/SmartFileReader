package com.scm.entities;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Document {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String fileName;
	@Column(unique = true , nullable = false)
	private String fileKey;
	private String contentType;
	@CreatedDate
	@Column(nullable = false,updatable = false)
	private LocalDateTime createdOn;
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedOn;
	@Column(nullable = false)
	private Long sizeInBytes;
	@Column
	private String thumbnailKey;
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	@OneToMany(mappedBy = "document",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
	private List<PdfAnnotation> annotations = new ArrayList<>() ;
	
}
