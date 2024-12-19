package com.example.LMS.controllers;

import com.example.LMS.models.*;
import com.example.LMS.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("/createQuiz")
    public ResponseEntity<String> createQuiz(@RequestBody QuizModel quiz) {
        quizService.createQuiz(quiz);
        return ResponseEntity.ok("Quiz created successfully");
    }

    @PostMapping("/{quizId}/addQuestion")
    public ResponseEntity<String> addQuestion(@PathVariable Long quizId, @RequestBody QuestionModel question) {
        Optional<QuizModel> quiz = quizService.getQuizById(quizId);

        if (quiz.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Quiz with ID " + quizId + " not found.");
        }

        quizService.addQuestionToQuiz(quizId, question);
        return ResponseEntity.ok("Question added to quiz");
    }


    @GetMapping("/{quizId}/randomQuestions")
    public ResponseEntity<List<QuestionModel>> getRandomQuestions(@PathVariable Long quizId, @RequestParam int numberOfQuestions) {
        Optional<QuizModel> quiz = quizService.getQuizById(quizId);

        if (quiz.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }

        List<QuestionModel> questions = quizService.getRandomQuestions(quizId, numberOfQuestions);
        return ResponseEntity.ok(questions);
    }

}
