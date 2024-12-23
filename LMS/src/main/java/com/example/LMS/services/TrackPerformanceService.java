package com.example.LMS.services;

import com.example.LMS.models.*;
import com.example.LMS.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrackPerformanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private QuizRepository quizRepository;

    // Fetch performance for courses based on attendance
    public List<Map<String, Object>> getPerformanceForCourses(List<String> courseNames, String lessonName) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (String courseName : courseNames) {
            Optional<CourseModel> courseOpt = courseRepository.findByTitle(courseName);
            if (courseOpt.isPresent()) {
                CourseModel course = courseOpt.get();

                Optional<LessonModel> lessonOpt = course.getListLessons().stream()
                        .filter(lesson -> lesson.getTitle().equals(lessonName))
                        .findFirst();

                if (lessonOpt.isPresent()) {
                    LessonModel lesson = lessonOpt.get();
                    List<AttendanceModel> attendanceRecords = attendanceRepository.findByLesson(lesson);

                    Map<String, Object> coursePerformance = new HashMap<>();
                    coursePerformance.put("Course", course.getTitle());

                    List<Map<String, Object>> performanceDetails = new ArrayList<>();

                    for (AttendanceModel record : attendanceRecords) {
                        Map<String, Object> performanceDetail = new HashMap<>();
                        performanceDetail.put("Student ID", record.getStudent().getId());
                        performanceDetail.put("Attendance Status", record.isAttended() ? "Attended" : "Not Attended");
                        performanceDetail.put("Lesson", lesson.getTitle());
                        performanceDetail.put("Date", record.getTimestamp().toLocalDate());

                        performanceDetails.add(performanceDetail);
                    }

                    coursePerformance.put("Performance", performanceDetails);
                    result.add(coursePerformance);
                }
            }
        }
        return result;
    }

    public Map<String, Object> getAssignment_Submitions(Integer assignmentId) {
        Optional<Assignment> assignmentOpt = assignmentRepository.findById(assignmentId);

        if (assignmentOpt.isPresent()) {
            Assignment assignment = assignmentOpt.get();

            // Creating the response map
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("assignmentId", assignment.getAssignmentID());
            result.put("assignmentTitle", assignment.getTitle());

            // Fetching students who submitted the assignment
            List<StudentModel> students = studentRepository.findAll(); // Adjust if there's a direct relation with Assignment
            List<Map<String, Object>> studentsGrades = new ArrayList<>();

            for (StudentModel student : students) {
                Map<String, Object> studentDetails = new HashMap<>();
                studentDetails.put("studentId", student.getId());
                studentDetails.put("studentName", student.getName());

                // Check if the student has submitted the assignment
                boolean hasSubmitted = assignment.getSubmittedStudents() != null &&
                        assignment.getSubmittedStudents().stream()
                                .anyMatch(submittedStudent -> submittedStudent.getId().equals(student.getId()));

                studentDetails.put("hasSubmitted", hasSubmitted ? "Submitted" : "Not Submitted");

                // If submitted, add grade and feedback
                if (hasSubmitted) {
                    studentDetails.put("grade", assignment.getGrades());
                    studentDetails.put("feedback", assignment.getFeedback());
                }

                studentDetails.put("date", new Date().toString()); // Replace with actual submission date if available

                // Fetch the course for the student
                List<CourseModel> courses = courseRepository.findByStudentId(Long.valueOf(student.getId())); // Ensure Long type
                if (!courses.isEmpty()) {
                    studentDetails.put("courseId", courses.get(0).getId());  // Add the course ID to the student details
                } else {
                    studentDetails.put("courseId", "No course found");
                }
                studentsGrades.add(studentDetails);
            }

            result.put("students", studentsGrades);
            return result;
        }

        throw new NoSuchElementException("Assignment not found with ID: " + assignmentId);
    }

    // Fetch quiz grades and feedback
    public Map<String, Object> getQuizGrades(long quizId) {
        Optional<QuizModel> quizOpt = quizRepository.findById(quizId);

        if (quizOpt.isPresent()) {
            QuizModel quiz = quizOpt.get();

            // Create the response map
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("quizId", quiz.getId());
            result.put("quizTitle", quiz.getQuizTitle());

            // Fetching students who took the quiz
            List<StudentModel> students = studentRepository.findAll(); // Adjust if there's a direct relation with Quiz
            List<Map<String, Object>> studentsGrades = new ArrayList<>();

            for (StudentModel student : students) {
                Map<String, Object> studentDetails = new HashMap<>();
                studentDetails.put("studentId", student.getId());
                studentDetails.put("studentName", student.getName());
                studentDetails.put("grade", quiz.getGrade());
                // studentDetails.put("feedback", quiz.getfeedback());
                studentDetails.put("date", new Date().toString()); // Replace with actual submission date if available

                List<CourseModel> courses = courseRepository.findByStudentId(Long.valueOf(student.getId())); // Ensure Long type
                if (!courses.isEmpty()) {
                    // Assuming the first course is the one to include; modify as necessary
                    studentDetails.put("courseId", courses.get(0).getId());
                } else {
                    // Handle case when no courses are found for the student
                    studentDetails.put("courseId", "No course found");
                }
                studentsGrades.add(studentDetails);
            }

            result.put("students", studentsGrades);
            return result;
        }

        throw new NoSuchElementException("Quiz not found with ID: " + quizId);
    }
}
