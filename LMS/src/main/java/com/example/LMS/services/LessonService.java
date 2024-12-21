package com.example.LMS.services;

import com.example.LMS.models.LessonModel;
import com.example.LMS.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    public void createLesson(LessonModel lessonModel) {
        lessonModel.updateEndDate();
        lessonRepository.save(lessonModel);
    }

    public List<LessonModel> displayLessons() {
        return lessonRepository.findAll();
    }

    public void generateOTP(String OTP, long lessonId) {
        LessonModel lesson = lessonRepository.findById(lessonId).get();
        lesson.setOTP(OTP);
    }
}
