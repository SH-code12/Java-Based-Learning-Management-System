package com.example.LMS.repositories;

import com.example.LMS.models.QuizModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<QuizModel, Long> {
    // Custom query methods can be added here if necessary
}