package com.leaps.exceptions;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LeapsApiException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	private String message;
	private HttpStatus status;
	

}
