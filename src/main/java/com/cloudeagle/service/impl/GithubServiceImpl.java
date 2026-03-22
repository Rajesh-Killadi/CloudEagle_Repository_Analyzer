package com.cloudeagle.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cloudeagle.dto.CollabratorDto;
import com.cloudeagle.dto.RepoDto;
import com.cloudeagle.service.GithubService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class GithubServiceImpl implements GithubService {

	
    private final OAuth2AuthorizedClientService clientService;
    
    private final ExecutorService executor = Executors.newFixedThreadPool(100);

    private final RestTemplate restTemplate;

    
    public String getAccessToken(Authentication authentication) {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        OAuth2AuthorizedClient client =
            clientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName()
            );

        return client.getAccessToken().getTokenValue();
    }
    
    
    public List<RepoDto> getRepositories(String org, String token) {
    	
        List<RepoDto> allRepos = new ArrayList<>();
        int page = 1;
        int perPage = 50;

        while (true) {
        	
            String url = "https://api.github.com/orgs/" + org + "/repos?per_page=" + perPage + "&page=" + page;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.set("Accept", "application/vnd.github+json");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<RepoDto[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    RepoDto[].class
            );

            RepoDto[] repos = response.getBody();
            
            if (repos == null || repos.length == 0) break;

            allRepos.addAll(List.of(repos));
            
            page++; 
        }

        return allRepos;
    }
    
    
    public List<CollabratorDto> getCollabrators(String org, String repo, String token) {
    	
        List<CollabratorDto> allCollabs = new ArrayList<>();
        
        int page = 1;
        int perPage = 100;

        while (true) {
            String collabUrl = "https://api.github.com/repos/" + org + "/" + repo + "/collaborators?per_page=" + perPage + "&page=" + page;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.set("Accept", "application/vnd.github+json");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<CollabratorDto[]> collabResponse = restTemplate.exchange(
                    collabUrl,
                    HttpMethod.GET,
                    entity,
                    CollabratorDto[].class
            );

            CollabratorDto[] collabs = collabResponse.getBody();
            if (collabs == null || collabs.length == 0) break;

            allCollabs.addAll(List.of(collabs));
            page++;
        }

        return allCollabs;
    }
        
    
    
    public Map<String, List<String>> generateAccessReport(String org, String token) {

    	List<RepoDto> repos = getRepositories(org, token);

        if (repos.isEmpty()) return new HashMap<>();

        Map<String, List<String>> report = new ConcurrentHashMap<>();

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (RepoDto repo : repos) {
        	
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            
            	List<CollabratorDto> collabrators = getCollabrators(org, repo.getName(), token );
               

                if (collabrators != null) {

                    for (CollabratorDto dto : collabrators) {

                        report.computeIfAbsent(dto.getLogin(),
                                k -> Collections.synchronizedList(new ArrayList<>()))
                              .add(repo.getName());
                    }
                }

            }, executor);

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return report;
    }
    
}

