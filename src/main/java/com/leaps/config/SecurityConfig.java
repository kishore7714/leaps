package com.leaps.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.leaps.security.JwtAuthenticationEntryPoint;
import com.leaps.security.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	
	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;
	
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()	
//		.authorizeHttpRequests((authorize)->authorize.anyRequest().authenticated())
		.authorizeHttpRequests((authorize)->authorize
				.requestMatchers(HttpMethod.GET,"/**").permitAll()
				.requestMatchers("/api/auth/**").permitAll()
				.anyRequest().authenticated()
				)
		.exceptionHandling(exception->exception.authenticationEntryPoint(authenticationEntryPoint))
		.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//		.httpBasic(Customizer.withDefaults());
		
		http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	//In Memory Authentication
//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails kishore = User.builder().username("Kishore").password(passwordEncoder().encode("Kishore")).roles("USER").build();
//		UserDetails admin = User.builder().username("Admin").password(passwordEncoder().encode("Admin")).roles("ADMIN").build();
//		return new InMemoryUserDetailsManager(kishore,admin);
//	}
}
