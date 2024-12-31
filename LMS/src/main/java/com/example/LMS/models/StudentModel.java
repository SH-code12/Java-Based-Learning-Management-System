package com.example.LMS.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity

public class StudentModel extends User {


    public StudentModel(Integer id, String name, String password, String role, String email) {
        super(id, name, password, role, email);
    }

    public StudentModel(String name,  String password,String role, String email) {
        super(name, role, password, email);
    }

    public StudentModel(Integer id, String email, String name) {
        super(id, email, name);
    }

    @ManyToMany(mappedBy = "students")
    @JsonIgnore // Prevent serialization of the circular reference
    private List<CourseModel> courses;

    public StudentModel() {

    }

    public StudentModel(String name, String password) {
        super(name, password);
    }

    public List<CourseModel> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseModel> courses) {
        this.courses = courses;
    }


}
