package com.example.LMS.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CourseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String courseId; // String as courseId
    private String title;
    private String description;
    private int durationHours;


    @OneToMany(mappedBy = "courseModel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Handles the forward part of the reference
    private List<LessonModel> listLessons = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "course_media_files", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "media_file")
    private List<String> mediaFiles = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Prevent circular reference
    private List<QuizModel> quizzes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonIgnore // Prevent circular reference
    private List<StudentModel> students = new ArrayList<>();

    // Constructors
    public CourseModel(String courseId, String title, String description, int durationHours, List<LessonModel> listLessons, List<String> mediaFiles) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.durationHours = durationHours;
        this.listLessons = listLessons;
        this.mediaFiles = mediaFiles != null ? mediaFiles : new ArrayList<>();
    }

    public CourseModel() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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

    public int getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(int durationHours) {
        this.durationHours = durationHours;
    }

    public List<LessonModel> getListLessons() {
        return listLessons;
    }

    public void setListLessons(List<LessonModel> listLessons) {
        this.listLessons = listLessons;
    }

    public List<String> getMediaFiles() {
        return mediaFiles;
    }

    public void setMediaFiles(List<String> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }

    public List<QuizModel> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<QuizModel> quizzes) {
        this.quizzes = quizzes;
    }

    public List<StudentModel> getStudents() {
        return students;
    }

    public void setStudents(List<StudentModel> students) {
        this.students = students;
    }

    // Additional Methods to manage quizzes
    public void addQuiz(QuizModel quiz) {
        this.quizzes.add(quiz);
    }

    public void addLesson(LessonModel lesson) {
        this.listLessons.add(lesson);
    }
}
