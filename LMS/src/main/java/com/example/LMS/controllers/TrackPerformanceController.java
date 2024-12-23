package com.example.LMS.controllers;

import com.example.LMS.services.TrackPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/performance")
public class TrackPerformanceController {

    @Autowired
    private TrackPerformanceService trackPerformanceService;

    // Endpoint for tracking performance based on attendance
    @PostMapping(value = "/track")
    public ResponseEntity<List<Map<String, Object>>> getPerformanceForCourses(
            @RequestBody PerformanceRequest request) {
        List<Map<String, Object>> performanceDetails = trackPerformanceService.getPerformanceForCourses(
                request.getCourseNames(), request.getLessonName());
        return ResponseEntity.ok(performanceDetails);
    }

    // Endpoint for fetching assignment grades and feedback
    @GetMapping(value = "/assignments/{assignmentId}")
    public ResponseEntity<Map<String, Object>> getAssignment_Submitions(@PathVariable Integer assignmentId) {
        Map<String, Object> assignmentGrades = trackPerformanceService.getAssignment_Submitions(assignmentId);
        return ResponseEntity.ok(assignmentGrades);
    }

    @GetMapping(value = "/quizes/{QuizId}")
    public ResponseEntity<Map<String, Object>> getQuizGrades(@PathVariable long QuizId) {
        Map<String, Object> quizGrades = trackPerformanceService.getQuizGrades(QuizId);
        return ResponseEntity.ok(quizGrades);
    }

    // Request class for tracking performance
    public static class PerformanceRequest {
        private List<String> courseNames;
        private String lessonName;

        // Getters and setters
        public List<String> getCourseNames() {
            return courseNames;
        }

        public void setCourseNames(List<String> courseNames) {
            this.courseNames = courseNames;
        }

        public String getLessonName() {
            return lessonName;
        }

        public void setLessonName(String lessonName) {
            this.lessonName = lessonName;
        }
    }
}
