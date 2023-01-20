package com.leaps.service.auth;

import com.leaps.dto.auth.LoginDto;
import com.leaps.dto.auth.SignUpDto;

public interface AuthService {

	String login(LoginDto loginDto);
	
	String signup (SignUpDto signUpDto);
}
