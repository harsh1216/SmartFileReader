package com.scm.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

	public static String getEmailOfLoggedInUser(Authentication authentication) {

		if (authentication instanceof OAuth2AuthenticationToken) {

			var aOAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
			var clientId = aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

			var oauth2User = (OAuth2User) authentication.getPrincipal();
			String email = null;

			if (clientId.equalsIgnoreCase("google")) {

				email = oauth2User.getAttribute("email").toString();
				System.out.println("Google account " + email);
			} else if (clientId.equalsIgnoreCase("github")) {

				email = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email").toString()
						: oauth2User.getAttribute("login").toString() + "@gmail.com";
				System.out.println("Github account " + email);
			}
			return email;

		} else {
			System.out.println("Self account " + authentication.getName());
			return authentication.getName();
		}
	}

	public static String getProfilePic(Authentication authentication) {
		if (authentication instanceof OAuth2AuthenticationToken) {
			var oauthToken = (OAuth2AuthenticationToken) authentication;
			var clientId = oauthToken.getAuthorizedClientRegistrationId();
			var oauth2User = (OAuth2User) authentication.getPrincipal();

			if ("google".equalsIgnoreCase(clientId)) {
				return "/user/profile-pic";
			} else if ("github".equalsIgnoreCase(clientId)) {
				String avatarUrl = oauth2User.getAttribute("avatar_url");
				return avatarUrl != null ? avatarUrl : "/images/default-avatar.png";
			}
		}
		return "/images/default-avatar.png"; // self accounts fallback
	}

}
