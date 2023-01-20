package com.leaps.controller.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leaps.dto.auth.JwtAuthResponse;
import com.leaps.dto.auth.LoginDto;
import com.leaps.dto.auth.SignUpDto;
import com.leaps.entity.admin.Users;
import com.leaps.repository.admin.UsersRepository;
import com.leaps.service.auth.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthService authService;
	@Autowired
	private UsersRepository usersRepository;
	
	//Build Login Rest Api
	@PostMapping(value = {"/login","/signin"})
	public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
		String token = authService.login(loginDto);
		
		Optional<Users> user = usersRepository.findByUsernameOrEmail(loginDto.getUsernameOrEmail(), loginDto.getUsernameOrEmail());
		
		JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
		jwtAuthResponse.setUsername(user.get().getUsername());
		jwtAuthResponse.setCompanyName(user.get().getCompany().getCompanyName());
		jwtAuthResponse.setAccessToken(token);
		HttpHeaders headers = new HttpHeaders();
		headers.add("description", "Login");
		return new ResponseEntity<>(jwtAuthResponse,headers,HttpStatus.OK);
	}
	
	//Build Register Rest Api
	@PostMapping(value = {"/signup","register"})
	public ResponseEntity<String> signup(@RequestBody SignUpDto signUpDto){
		String response = authService.signup(signUpDto);
		HttpHeaders headers = new HttpHeaders();
		headers.add("description", "SignUp or Register");
		return new ResponseEntity<String>(response,HttpStatus.CREATED);
	}

}
