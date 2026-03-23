package com.cloudeagle.reporter.service;

import com.cloudeagle.reporter.model.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubService {
    
    private final WebClient webClient;
    
    public List<Repository> getRepositories(String organization) {
        log.info("Fetching repositories for: {}", organization);
        
        Flux<Repository> repositoryFlux = webClient.get()
                .uri("/orgs/{org}/repos?per_page=100", organization)
                .retrieve()
                .bodyToFlux(Repository.class);
        
        return repositoryFlux.collectList().block();
    }
    
    public List<Collaborator> getCollaborators(String organization, String repoName) {
        log.info("Fetching collaborators for: {}/{}", organization, repoName);
        
        Flux<Collaborator> collaboratorFlux = webClient.get()
                .uri("/repos/{org}/{repo}/collaborators", organization, repoName)
                .retrieve()
                .bodyToFlux(Collaborator.class);
        
        return collaboratorFlux.collectList().block();
    }
}