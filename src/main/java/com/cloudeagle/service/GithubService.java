package com.cloudeagle.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;

import com.cloudeagle.dto.CollabratorDto;
import com.cloudeagle.dto.RepoDto;

public interface GithubService {
	
	public String getAccessToken(Authentication authentication);

	public RepoDto[] getRepositories(String org, String token);

	public CollabratorDto[] getCollabrators(String org, String repo, String token);

	public Map<String, List<String>> generateAccessReport(String org, String token);

}