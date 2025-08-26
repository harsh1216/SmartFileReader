package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.repositories.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenicationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	UserRepository userRepo;

	Logger logger = org.slf4j.LoggerFactory.getLogger(OAuthAuthenicationSuccessHandler.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		logger.info("OAuthAuthenicationSuccessHandler");

		var oauth2authenicationToken = (OAuth2AuthenticationToken) authentication;

		String authorizedClientRegistrationId = oauth2authenicationToken.getAuthorizedClientRegistrationId();

		logger.info(authorizedClientRegistrationId);

		DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

		user.getAttributes().forEach((key, value) -> {
			logger.info("{} => {}", key, value);
			return;
		});
		User user1 = new User();
		user1.setActive(true);
		user1.setEmailVerfied(true);
		user1.setEnabled(true);
		user1.setUserId(UUID.randomUUID().toString());
		user1.setRoleList(List.of(AppConstants.ROLE_USER));

		if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
			user1.setName(user.getAttribute("name").toString());
			user1.setEmail(user.getAttribute("email").toString());
			user1.setProfilePic(user.getAttribute("picture").toString());
			user1.setProviderUserId(user.getName().toString());
			user1.setProvider(Providers.GOOGLE);
			user1.setAbout("Account created using Google");
		} else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
			String email = user.getAttribute("email") != null ? user.getAttribute("email").toString()
					: user.getAttribute("login").toString() + "@gmail.com";
			user1.setEmail(email);
			user1.setName(user.getAttribute("name").toString());
			user1.setProfilePic(user.getAttribute("avatar_url").toString());
			user1.setProviderUserId(user.getAttribute("id").toString());
			user1.setProvider(Providers.GITHUB);
			user1.setAbout("Account created using Github");
		} else {
			return;
		}

		User userDb = userRepo.findByEmail(user1.getEmail()).orElse(null);

		if (userDb == null) {
			userRepo.save(user1);
		}

		logger.info(user.getAuthorities().toString());

		new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");

	}

}
