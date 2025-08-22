package com.scm.dtos;

import com.scm.entities.PdfAnnotationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class PdfAnnotationDTO {

	private Long id;
	private PdfAnnotationType type;
	private int pageName;
	private Float x;
	private Float y;
	private Float width;
	private Float height;
	private String text;
    private String color;
}
