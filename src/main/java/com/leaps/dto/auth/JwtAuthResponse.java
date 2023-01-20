package com.leaps.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthResponse {
	private String username;
	private String companyName;
	private String tokenType="Bearer";
	private String accessToken;
}
