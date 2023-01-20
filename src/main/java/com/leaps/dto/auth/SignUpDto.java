package com.leaps.dto.auth;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {
	private String username;
	private String email;
	private String password;
	private Long companyId;
	@Lob
	private String profile;

	private String verificationCode;
}
