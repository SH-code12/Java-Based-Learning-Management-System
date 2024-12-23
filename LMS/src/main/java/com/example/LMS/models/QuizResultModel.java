package com.example.LMS.models;

import java.util.Map;

public class QuizResultModel {
    private QuizModel quiz;
    private Map<Long, String> userAnswers; // Question ID to User's answer
    private double grade;
    private int totalQuestions;
    private int correctAnswers;

    // Getters and Setters
    public QuizModel getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizModel quiz) {
        this.quiz = quiz;
    }

    public Map<Long, String> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(Map<Long, String> userAnswers) {
        this.userAnswers = userAnswers;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
}