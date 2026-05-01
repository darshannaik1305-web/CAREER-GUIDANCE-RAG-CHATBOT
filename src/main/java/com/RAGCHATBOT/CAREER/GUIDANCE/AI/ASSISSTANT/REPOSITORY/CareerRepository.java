package com.RAGCHATBOT.CAREER.GUIDANCE.AI.ASSISSTANT.REPOSITORY;

import com.RAGCHATBOT.CAREER.GUIDANCE.AI.ASSISSTANT.ENTITY.Career;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CareerRepository extends JpaRepository<Career, Long> {

    List<Career> findByRoleContainingIgnoreCase(String keyword);
}