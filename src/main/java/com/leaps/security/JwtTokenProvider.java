package com.leaps.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.leaps.exceptions.LeapsApiException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${app.jwt-secret}")
	private String jwtSecret;
	@Value("${app-jwt-expiration-milliseconds}")
	private Long jwtExpirationDate;

	// Generate Jwt Token

	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currenDate = new Date();
		Date expirationDate = new Date(currenDate.getTime() + jwtExpirationDate);

		String token = Jwts.builder().setSubject(username).setIssuedAt(currenDate).setExpiration(expirationDate)
				.signWith(key()).compact();
		return token;
	}

	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	// Get username from Jwt token
	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();

		String username = claims.getSubject();
		return username;
	}

	// Validate Jwt Token
	public Boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(token);

			return true;
		} catch (MalformedJwtException e) {
			throw new LeapsApiException("Invalid Jwt Token", HttpStatus.BAD_REQUEST);
		} catch (ExpiredJwtException e) {
			throw new LeapsApiException("Expired Jwt Token", HttpStatus.BAD_REQUEST);
		} catch (UnsupportedJwtException e) {
			throw new LeapsApiException("Unsupported Jwt Token", HttpStatus.BAD_REQUEST);
		} catch (IllegalArgumentException e) {
			throw new LeapsApiException("Jwt Claims String is Empty", HttpStatus.BAD_REQUEST);
		}

	}

}
