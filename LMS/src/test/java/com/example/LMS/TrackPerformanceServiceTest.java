package com.example.LMS;

import com.example.LMS.models.*;
import com.example.LMS.repositories.*;
import com.example.LMS.services.QuizService;
import com.example.LMS.services.TrackPerformanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrackPerformanceServiceTest {

    @InjectMocks
    private TrackPerformanceService trackPerformanceService;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private QuizService quizService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPerformanceForCourses() {
        String courseName = "Math";
        String lessonName = "Lesson 1";

        CourseModel mockCourse = new CourseModel();
        mockCourse.setTitle(courseName);

        LessonModel mockLesson = new LessonModel();
        mockLesson.setTitle(lessonName);
        mockCourse.setListLessons(Collections.singletonList(mockLesson));

        AttendanceModel mockAttendance = new AttendanceModel();
        StudentModel mockStudent = new StudentModel();
        mockStudent.setId(1);
        mockStudent.setName("John Doe");
        mockAttendance.setStudent(mockStudent);
        mockAttendance.setAttended(true);
        mockAttendance.setTimestamp(LocalDateTime.now());
        mockAttendance.setLesson(mockLesson);

        when(courseRepository.findByTitle(courseName)).thenReturn(Optional.of(mockCourse));
        when(attendanceRepository.findByLesson(mockLesson)).thenReturn(Collections.singletonList(mockAttendance));

        List<Map<String, Object>> result = trackPerformanceService.getPerformanceForCourses(Collections.singletonList(courseName), lessonName);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(courseName, result.get(0).get("Course"));
    }

    @Test
    void testGetAssignmentGrades() {
        int assignmentId = 1;

        Assignment mockAssignment = new Assignment();
        mockAssignment.setAssignmentID(assignmentId);
        mockAssignment.setTitle("Assignment 1");
        mockAssignment.setGrades(95);
        mockAssignment.setFeedback("Well done!");

        StudentModel mockStudent = new StudentModel();
        mockStudent.setId(1);
        mockStudent.setName("John Doe");

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(mockAssignment));
        when(studentRepository.findAll()).thenReturn(Collections.singletonList(mockStudent));

        Map<String, Object> result = trackPerformanceService.getAssignmentGrades(assignmentId);

        assertNotNull(result);
        assertEquals(assignmentId, result.get("assignmentId"));
        assertEquals("Assignment 1", result.get("assignmentTitle"));
        assertNotNull(result.get("students"));
        assertEquals(1, ((List<?>) result.get("students")).size());
    }

    @Test
    void testGetAssignmentGrades_AssignmentNotFound() {
        int assignmentId = 1;

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                trackPerformanceService.getAssignmentGrades(assignmentId)
        );

        assertEquals("Assignment not found with ID: 1", exception.getMessage());
    }

    @Test
    void testGetQuizGrades() {
        long quizId = 1L;

        QuizModel mockQuiz = new QuizModel();
        mockQuiz.setId(quizId);
        mockQuiz.setQuizTitle("Quiz 1");
        mockQuiz.setGrade(90);

        StudentModel mockStudent = new StudentModel();
        mockStudent.setId(1);
        mockStudent.setName("John Doe");

        CourseModel mockCourse = new CourseModel();
        mockCourse.setId(101L);

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(mockQuiz));
        when(studentRepository.findAll()).thenReturn(Collections.singletonList(mockStudent));
        when(courseRepository.findByStudentId(1L)).thenReturn(Collections.singletonList(mockCourse));

        Map<String, Object> result = trackPerformanceService.getQuizGrades(quizId);

        assertNotNull(result);
        assertEquals(quizId, result.get("quizId"));
        assertEquals("Quiz 1", result.get("quizTitle"));
        assertNotNull(result.get("students"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> students = (List<Map<String, Object>>) result.get("students");
        assertEquals(1, students.size());

        Map<String, Object> studentDetails = students.get(0);
        assertEquals(1, studentDetails.get("studentId"));
        assertEquals("John Doe", studentDetails.get("studentName"));
        assertEquals(90.0, studentDetails.get("grade"));
        assertEquals(101L, studentDetails.get("courseId"));
    }

    @Test
    void testGetQuizGrades_QuizNotFound() {
        long quizId = 1L;

        when(quizRepository.findById(quizId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                trackPerformanceService.getQuizGrades(quizId)
        );

        assertEquals("Quiz not found with ID: " + quizId, exception.getMessage());
    }
}
