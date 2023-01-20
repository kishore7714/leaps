package com.leaps.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.leaps.entity.admin.Users;
import com.leaps.repository.admin.UsersRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UsersRepository usersRepository;
	
	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		Users user = usersRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
		.orElseThrow(()->new UsernameNotFoundException("User Not Found With Username or Email: "+usernameOrEmail));
		Set<SimpleGrantedAuthority> authorities = user.getUserGroups().stream()
		.map(usergroup->new SimpleGrantedAuthority(usergroup.getUserGroupName())).collect(Collectors.toSet());
		return new User(user.getEmail(), user.getPassword(), authorities);
	}

}
