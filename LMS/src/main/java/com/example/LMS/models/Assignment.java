package com.example.LMS.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assignmentID;
    private String title;
    private String description;
    private String deadline;
    private double grades;
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = true) // Foreign key to Course table
    private CourseModel course;

    @ManyToMany
    @JoinTable(
            name = "assignment_submissions",  // The name of the join table
            joinColumns = @JoinColumn(name = "assignment_id"),  // Foreign key for Assignment
            inverseJoinColumns = @JoinColumn(name = "student_id")  // Foreign key for Student
    )
    private List<StudentModel> submittedStudents;

    // Getters and Setters

    public Assignment() {}

    public Assignment(Integer assignmentID, String title, String description, String deadline, double grades, String feedback, CourseModel course) {
        this.assignmentID = assignmentID;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.grades = grades;
        this.feedback = feedback;
        this.course = course;
    }

    public Integer getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(Integer assignmentID) {
        this.assignmentID = assignmentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getGrades() {
        return grades;
    }

    public void setGrades(double grades) {
        this.grades = grades;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public CourseModel getCourse() {
        return course;
    }

    public void setCourse(CourseModel course) {
        this.course = course;
    }

    public List<StudentModel> getSubmittedStudents() {
        return submittedStudents;
    }

    public void setSubmittedStudents(List<StudentModel> submittedStudents) {
        this.submittedStudents = submittedStudents;
    }
}
