package com.cloudeagle.reporter.model;

import lombok.Data;

@Data
public class Collaborator {
    private Long id;
    private String login;
    private String role;
}