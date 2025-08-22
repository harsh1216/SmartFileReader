package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.services.impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

	@Autowired
	SecurityCustomUserDetailService userDetailService;

	@Autowired
	OAuthAuthenicationSuccessHandler authAuthenicationSuccessHandler;

//	configure authentication
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

		return daoAuthenticationProvider;

	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//		configure
		httpSecurity.authorizeHttpRequests(authorize -> {
			// authorize.requestMatchers("/").permitAll();
			authorize.requestMatchers("/user/**").authenticated();
			authorize.anyRequest().permitAll();
		});

//		form default login
		httpSecurity.formLogin(loginForm -> {
			loginForm.loginPage("/login").loginProcessingUrl("/authenticate").defaultSuccessUrl("/user/dashboard", true)
					.failureUrl("/login?error=true").usernameParameter("email").passwordParameter("password");
		});

		httpSecurity.csrf(AbstractHttpConfigurer::disable);

		httpSecurity.logout(logoutform -> {
			logoutform.logoutUrl("/logout");
		});
//		OAuth Login
		httpSecurity.oauth2Login(oauth -> {
			oauth.loginPage("/login").successHandler(authAuthenicationSuccessHandler).failureUrl("/login?error=true");
		});

//		httpSecurity.oauth2Login(Customizer.withDefaults());
		httpSecurity.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));	
		return httpSecurity.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
