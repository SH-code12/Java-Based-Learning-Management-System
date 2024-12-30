package com.example.LMS.controllers;

import com.example.LMS.DTOs.CourseDTO;
import com.example.LMS.DTOs.StudentDTO;
import com.example.LMS.models.*;
import com.example.LMS.repositories.UserRepository;
import com.example.LMS.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController
{
    @Autowired
    final StudentService studentService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    UserRepository userRepository;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @Autowired
    private TrackPerformanceService trackPerformanceService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private QuizService quizService;
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping
    public ResponseEntity< List<StudentDTO> >retrieveAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<StudentModel>  retrieveStudentById(@PathVariable int id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }
    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("/enrollCourse")
    public ResponseEntity<String> enrollCourse(@RequestParam("student_id") int studentId, @RequestParam("Course_id") long courseid) {
        System.out.println(studentId);
        studentService.enrollStudent(studentId ,  courseid);
        return  ResponseEntity.ok("Student Enroll successfully ");

    }
    @PreAuthorize("hasAuthority('STUDENT')")
    // Endpoint for fetching assignment grades and feedback
    @GetMapping(value = "/assignments/{assignmentId}")
    public ResponseEntity<Map<String, Object>> getAssignmentGrades(@PathVariable Integer assignmentId) {
        Map<String, Object> assignmentGrades = trackPerformanceService.getAssignment_Submitions(assignmentId);
        return ResponseEntity.ok(assignmentGrades);
    }
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping(value = "/quizes/{QuizId}")
    public ResponseEntity<Map<String, Object>> getQuizGrades(@PathVariable long QuizId) {
        Map<String, Object> quizGrades = trackPerformanceService.getQuizGrades(QuizId);
        return ResponseEntity.ok(quizGrades);
    }
    //submit quiz , assignment
    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("/submitAssignment")
    public ResponseEntity<Assignment> submitAssignment(@RequestBody Assignment assignment) {
        Assignment submittedAssignment = assignmentService.submitAssignment(assignment);
        return ResponseEntity.ok(submittedAssignment);
    }
    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("/submitQuiz")
    public ResponseEntity<QuizModel> submitQuiz(@RequestBody QuizModel quiz) {
        QuizModel submittedQuiz = quizService.submitQuiz(quiz);
        return ResponseEntity.ok(submittedQuiz);
    }
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/displayCourses")
    public ResponseEntity<List<CourseDTO>> displayCourses() {
        List<CourseDTO> courses = courseService.displayCourses();
        return ResponseEntity.ok(courses);
    }
    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("/attend-lesson")
    public ResponseEntity<String> attendLesson(@RequestParam int studentId, @RequestParam Long lessonId, @RequestParam String OTP) {
        return ResponseEntity.ok(attendanceService.attendLesson(studentId, lessonId, OTP));
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/{courseId}/materials")
    public ResponseEntity<List<String>> getCourseMaterials(@PathVariable Long courseId) {
        List<String> mediaFiles = courseService.getMediaFilesByCourseId(courseId);
        if (mediaFiles.isEmpty()) {
            return ResponseEntity.status(404).body(null); // Return 404 if no materials are found
        }
        return ResponseEntity.ok(mediaFiles); // Return the list of media files for the course
    }
    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("/takeQuiz/{quizId}")
    public ResponseEntity<QuizResultModel> takeQuiz(
            @PathVariable Long quizId,
            @RequestBody Map<Long, String> userAnswers) { // Question ID -> Answer
        QuizResultModel result = quizService.takeQuiz(quizId, userAnswers);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

}
