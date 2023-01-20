package com.leaps.serviceImpl.auth;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.leaps.dto.auth.LoginDto;
import com.leaps.dto.auth.SignUpDto;
import com.leaps.entity.admin.UserGroup;
import com.leaps.entity.admin.Users;
import com.leaps.exceptions.LeapsApiException;
import com.leaps.repository.admin.UserGroupRepository;
import com.leaps.repository.admin.UsersRepository;
import com.leaps.security.JwtTokenProvider;
import com.leaps.service.auth.AuthService;

@Service
public class AuthServiceImpl implements AuthService{
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private UserGroupRepository userGroupRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public String login(LoginDto loginDto) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtTokenProvider.generateToken(authentication);
		
		return token;
	}

	@Override
	public String signup(SignUpDto signUpDto) {
		if(usersRepository.existsByUsername(signUpDto.getUsername())) {
			throw new LeapsApiException("Username Already Exists",HttpStatus.BAD_REQUEST);
		}
		if(usersRepository.existsByEmail(signUpDto.getEmail())) {
			throw new LeapsApiException("Email Already Exists", HttpStatus.BAD_REQUEST);
		}
		
		//Set values to User
		Users users = new Users();
		users.setEmail(signUpDto.getEmail());
		users.setUsername(signUpDto.getUsername());
		users.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
		users.setCompanyId(signUpDto.getCompanyId());
		users.setProfile(signUpDto.getProfile());
		users.setVerificationCode(signUpDto.getVerificationCode());
		users.setValidFlag(1);
		
		Set<UserGroup> userGroups = new HashSet<>();
		UserGroup userGroup = userGroupRepository.findByUserGroupName("ROLE_USER").get();
		userGroups.add(userGroup);
		users.setUserGroups(userGroups);
		usersRepository.save(users);
		
		return "User Registered Successfully";
	}

}
