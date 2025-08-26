package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

	@Autowired
	UserService userService;

	@GetMapping("/")
	public String openHomePage(Model model) {
		model.addAttribute("page", "homePage");
		return "homePage";
	}

	@GetMapping("/about")
	public String openAboutPage(Model model) {
		model.addAttribute("page", "about");
		return "about";
	}

	@GetMapping("/services")
	public String openServicesPage(Model model) {
		model.addAttribute("page", "about");
		return "services";
	}

	@GetMapping("/contact")
	public String openContactPage(Model model) {
		model.addAttribute("page", "about");
		return "about";
	}

	@GetMapping("/login")
	public String openLogintPage(Model model) {
		model.addAttribute("page", "about");
		return "login";
	}

	@GetMapping("/signUp")
	public String openSignUpPage(Model model, HttpSession session) {
		model.addAttribute("userForm", new UserForm());

		return "signUp";
	}

	@PostMapping("/signUpForm")
	public String signUpUser(@Valid @ModelAttribute("userForm") UserForm userForm, BindingResult rBindingResult,
			Model model, HttpSession session) {

		if (rBindingResult.hasErrors()) {
			return "/signUp";
		}

		User user = new User();
		user.setName(userForm.getName());
		user.setEmail(userForm.getEmail());
		user.setPassword(userForm.getPassword());

		userService.saveUser(user);

		Message message = new Message();
		message.setContent("User register successfully");
		message.setMessageType(MessageType.green);

		session.setAttribute("message", message);
		return "redirect:/login";
	}

}
