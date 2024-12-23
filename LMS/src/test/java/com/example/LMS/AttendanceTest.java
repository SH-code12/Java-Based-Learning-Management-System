package com.example.LMS;

import com.example.LMS.models.AttendanceModel;
import com.example.LMS.models.CourseModel;
import com.example.LMS.models.LessonModel;
import com.example.LMS.models.StudentModel;
import com.example.LMS.repositories.CourseRepository;
import com.example.LMS.repositories.LessonRepository;
import com.example.LMS.repositories.StudentRepository;
import com.example.LMS.services.AttendanceService;
import com.example.LMS.services.StudentService;
import jakarta.transaction.Transactional;
import org.apache.catalina.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class AttendanceTest {
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;

    CourseModel course;
    private StudentModel student;
    private LessonModel lesson;
    @Autowired
    private CourseRepository courseRepository;


    @BeforeEach
    void setUp() {
        student = new StudentModel("Amr", "amr.mohammed@gmail.com");
        lesson = new LessonModel("c++#1", Arrays.asList("Data types", "Varibles"), "Introduction to c++ basics", "Dr.Ali", LocalDateTime.of(2024, 1, 15, 10, 30), 60, "LESS#120");
        course = new CourseModel("CS", "Computer Science", "Learn the fundamentals of CS", 40, List.of(lesson), List.of("lec1.pdf"));
        lesson.setCourseModel(course);
        studentRepository.save(student);
        lessonRepository.save(lesson);
        courseRepository.save(course);
    }

    @Test
    public void lessonNotOngoingTest() {
        String result = attendanceService.attendLesson(student.getId(), lesson.getId(), lesson.getOTP());
        assertEquals("Lesson is not ongoing.", result);
    }
    @Test
    public void OTPNotCorrectTest() {
        lesson.setStartDate(LocalDateTime.now());
        String result = attendanceService.attendLesson(student.getId(), lesson.getId(), "NotValidOTP");
        assertEquals("Lesson is not ongoing.", result);
    }
    @Test
    public void attendLessonSuccessfullyTest() {
        lesson.setStartDate(LocalDateTime.now());
        student.setCourses(List.of(course));
        course.setStudents(List.of(student));
        String result = attendanceService.attendLesson(student.getId(), lesson.getId(), lesson.getOTP());
        assertEquals("Lesson is not ongoing.", result);
    }

    @Test
    public void lessonNotFoundTest() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> attendanceService.attendLesson(student.getId(), -1, lesson.getOTP()));
        assertEquals("Lesson not found", exception.getMessage());
    }
    @Test
    public void studentNotFoundTest() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> attendanceService.attendLesson(-1, lesson.getId(), lesson.getOTP()));
        assertEquals("Lesson not found", exception.getMessage());
    }
}
