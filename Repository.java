package com.cloudeagle.reporter.model;

import lombok.Data;

@Data
public class Repository {
    private Long id;
    private String name;
    private String fullName;
    private boolean archived;
    private boolean privateRepo;
}
