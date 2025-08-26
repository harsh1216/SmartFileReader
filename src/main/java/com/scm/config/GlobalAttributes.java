package com.scm.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalAttributes {

	@ModelAttribute("currentUri")
	public String currentUri(HttpServletRequest request) {
		System.out.println("Current Url: " + request.getRequestURI());
		return request.getRequestURI();
	}
}
