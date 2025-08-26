package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.impl.UserServiceImpl;

@ControllerAdvice
public class RootController {

	@Autowired
	UserServiceImpl userServiceImpl;

	Logger logger = LoggerFactory.getLogger(getClass());

	@ModelAttribute
	public void getLoggedInUser(Model model, Authentication authentication) {
		if (authentication == null) {
			return;
		}
		;
		String email = Helper.getEmailOfLoggedInUser(authentication);

		User user = userServiceImpl.getUserByEmail(email);

		model.addAttribute("user", user);
	}

	@ModelAttribute
	public void addGlobalAttributes(Model model, Authentication authentication) {
		if (authentication != null) {
			String image = Helper.getProfilePic(authentication);
			logger.info("image is :" + image);
			model.addAttribute("profilePic", image);
		}
	}

}
