package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForm {
	@NotBlank(message = "Name cannot be blank")
	@Size(min = 3, message = "Name should we more then 3 characters")
	private String name;
	@Email
	@NotBlank(message = "Email cannot be blank")
	private String email;
	@NotBlank(message = "Password cannot be blank")
	private String password;

}
