package com.RAGCHATBOT.CAREER.GUIDANCE.AI.ASSISSTANT.ENTITY;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "career")
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;              // Software Engineer
    private String skillsRequired;    // Java, DSA, Spring Boot
    private String roadmap;           // Step-by-step path
    private String resources;         // Courses, links

}