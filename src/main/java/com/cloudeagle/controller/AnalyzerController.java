package com.cloudeagle.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cloudeagle.service.GithubService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AnalyzerController {

	
	private final GithubService service;

	
	@GetMapping("/report/{orgName}")
	public ResponseEntity<Map<String, List<String>>> getRepositiories(Authentication auth, @PathVariable String orgName){
		
		
		String accessToken = service.getAccessToken(auth);
		
		
		Map<String, List<String>> accessReport = service.generateAccessReport(orgName, accessToken);
		
		return ResponseEntity.status(HttpStatus.OK).body(accessReport);
		
	}

	

}
