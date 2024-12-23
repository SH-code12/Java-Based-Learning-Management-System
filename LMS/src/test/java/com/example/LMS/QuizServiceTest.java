package com.example.LMS;

import com.example.LMS.models.Assignment;
import com.example.LMS.models.QuestionModel;
import com.example.LMS.models.QuizModel;
import com.example.LMS.models.QuizResultModel;
import com.example.LMS.repositories.QuestionRepository;
import com.example.LMS.repositories.QuizRepository;
import com.example.LMS.services.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuizService quizService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateQuiz() {
        QuizModel quiz = new QuizModel();
        quiz.setId(1L);
        quiz.setQuizTitle("Test Quiz");

        quizService.createQuiz(quiz);

        verify(quizRepository, times(1)).save(quiz);
    }

    @Test
    void testAddQuestionToQuiz() {
        QuizModel quiz = new QuizModel();
        quiz.setId(1L);
        quiz.setQuestions(new ArrayList<>());

        QuestionModel question = new QuestionModel();
        question.setId(null); // Simulate a new question
        question.setQuestionText("Sample Question");

        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(questionRepository.save(question)).thenAnswer(invocation -> {
            question.setId(1L); // Simulate database assigning an ID
            return question;
        });

        quizService.addQuestionToQuiz(1L, question);

        verify(questionRepository, times(1)).save(question);
        verify(quizRepository, times(1)).save(quiz);

        assertEquals(1, quiz.getQuestions().size());
        assertEquals(question, quiz.getQuestions().get(0));
        assertEquals(quiz, question.getQuiz());
    }


    @Test
    void testGetRandomQuestions() {
        QuizModel quiz = new QuizModel();
        quiz.setId(1L);

        QuestionModel q1 = new QuestionModel();
        q1.setId(1L);
        q1.setQuestionText("Question 1");

        QuestionModel q2 = new QuestionModel();
        q2.setId(2L);
        q2.setQuestionText("Question 2");

        quiz.setQuestions(new ArrayList<>(Arrays.asList(q1, q2)));

        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));

        List<QuestionModel> randomQuestions = quizService.getRandomQuestions(1L, 1);

        assertEquals(1, randomQuestions.size());
        assertTrue(quiz.getQuestions().contains(randomQuestions.get(0)));
    }

    @Test
    void testGetQuizById() {
        QuizModel quiz = new QuizModel();
        quiz.setId(1L);

        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));

        Optional<QuizModel> fetchedQuiz = quizService.getQuizById(1L);

        assertTrue(fetchedQuiz.isPresent());
        assertEquals(quiz, fetchedQuiz.get());
    }

    @Test
    void testGetQuizById_NotFound() {
        when(quizRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<QuizModel> fetchedQuiz = quizService.getQuizById(1L);

        assertFalse(fetchedQuiz.isPresent());
    }
    @Test
    void testGradeQuiz() {
        QuizModel quiz = new QuizModel();
        // Given: Mock the repository behavior
        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));  // Simulate quiz retrieval by ID

        // When: Update the grade of the quiz
        double newGrade = 85.5;
        quiz.setGrade(newGrade);  // Set the grade for the quiz

        when(quizRepository.save(quiz)).thenReturn(quiz);  // Simulate saving the quiz

        // Call the grading logic (assuming you have a gradeQuiz method)
        quizService.gradeQuiz(1L, newGrade);

        // Then: Verify the grade is correctly set
        assertEquals(newGrade, quiz.getGrade(), "The grade should be updated correctly.");
        verify(quizRepository, times(1)).save(quiz);  // Ensure save was called once
    }
    @Test
    void takeQuiz_ShouldReturnCorrectResult_WhenQuizExists() {
        // Mock data
        Long quizId = 1L;

        QuestionModel question1 = new QuestionModel();
        question1.setId(1L);
        question1.setCorrectAnswer("A");

        QuestionModel question2 = new QuestionModel();
        question2.setId(2L);
        question2.setCorrectAnswer("B");

        QuizModel quiz = new QuizModel();
        quiz.setId(quizId);
        quiz.setQuestions(Arrays.asList(question1, question2));

        Map<Long, String> userAnswers = new HashMap<>();
        userAnswers.put(1L, "A");
        userAnswers.put(2L, "C");

        // Mocking repository
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        // Call the method
        QuizResultModel result = quizService.takeQuiz(quizId, userAnswers);

        // Assertions
        assertNotNull(result);
        assertEquals(50.0, result.getGrade());
        assertEquals(2, result.getTotalQuestions());
        assertEquals(1, result.getCorrectAnswers());
        assertEquals(quiz, result.getQuiz());
        assertEquals(userAnswers, result.getUserAnswers());

        // Verify repository interaction
        verify(quizRepository, times(1)).findById(quizId);
    }

    @Test
    void takeQuiz_ShouldReturnNull_WhenQuizDoesNotExist() {
        // Mock data
        Long quizId = 1L;
        Map<Long, String> userAnswers = new HashMap<>();
        userAnswers.put(1L, "A");

        // Mocking repository
        when(quizRepository.findById(quizId)).thenReturn(Optional.empty());

        // Call the method
        QuizResultModel result = quizService.takeQuiz(quizId, userAnswers);

        // Assertions
        assertNull(result);

        // Verify repository interaction
        verify(quizRepository, times(1)).findById(quizId);
    }

}
