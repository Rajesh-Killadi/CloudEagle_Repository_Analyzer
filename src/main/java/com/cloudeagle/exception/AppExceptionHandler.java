package com.cloudeagle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class AppExceptionHandler {

	
	private final String errorResponse = "Invalid Organization Name, Please Enter Valid Name";
	
	@ExceptionHandler(exception = HttpClientErrorException.class)
	public ResponseEntity<String> handleClientException(HttpClientErrorException ex){
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
}
