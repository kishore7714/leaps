package com.leaps.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersDto {
	private Long id;

	@NotBlank(message = "Username cannot be blank")
	@Size(max = 20, message = "Username field should contain maximum of 20 characters")
	private String username;

	@NotBlank(message = "Email field cannot be blank")
	@Size(max = 50, message = "E-mail should maximum of 50 characters")
	@Email(message = "Enter a valid e-mail")
	private String email;

	@NotBlank(message = "Password cannot be blank")
	@Size(max = 120, message = "Password should not exceed maximum of 120 Characters")
	private String password;

	private Long companyId;
	private String companyName;
//	private Long userGroupId;
//	private String userGroupName;
	private String profile;
	private String verificationCode;
}
