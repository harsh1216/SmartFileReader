package com.scm.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.scm.entities.Document;
import com.scm.entities.User;
import com.scm.services.DocumentService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	DocumentService documentService;

	@GetMapping("/fileManager")
	public String openUserfileManager(@ModelAttribute("user") User user, Model model) {

		System.out.println("User is: " + user.getName());
		List<Document> documents = documentService.getDocumentByUser(user);

		model.addAttribute("documentList", documents);

		Long totalSize = documents.stream().mapToLong(Document::getSizeInBytes).sum();

		model.addAttribute("totalSize", totalSize);

		return "user/fileManager";
	}

	@GetMapping("/profile")
	public String openUserProfile() {

		return "user/profile";
	}

	@GetMapping("/dashboard")
	public String openUserDashboard(@ModelAttribute("user") User user, Model model) {
		List<Document> documents = documentService.getDocumentByUser(user);

		model.addAttribute("documentList", documents);

		return "user/dashboard";
	}

	@GetMapping("/profile-pic")
	public ResponseEntity<byte[]> getGoogleProfilePic(OAuth2AuthenticationToken auth) {
		OAuth2User oauth2User = auth.getPrincipal();
		String pictureUrl = oauth2User.getAttribute("picture");

		if (pictureUrl != null) {
			RestTemplate restTemplate = new RestTemplate();
			byte[] imageBytes = restTemplate.getForObject(pictureUrl, byte[].class);

			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
		}
		return ResponseEntity.notFound().build();
	}

}
