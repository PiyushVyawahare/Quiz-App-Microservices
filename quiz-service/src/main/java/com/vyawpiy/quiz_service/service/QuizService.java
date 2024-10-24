package com.vyawpiy.quiz_service.service;

import com.vyawpiy.quiz_service.feign.QuizFeignClient;
import com.vyawpiy.quiz_service.model.QuestionWrapper;
import com.vyawpiy.quiz_service.model.Quiz;
import com.vyawpiy.quiz_service.model.QuizDto;
import com.vyawpiy.quiz_service.model.Response;
import com.vyawpiy.quiz_service.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuizFeignClient quizFeignClient;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Integer> questionIds = quizFeignClient.generateQuestionsForQuiz(category, numQ).getBody();

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questionIds);

        quizRepository.save(quiz);

        return new ResponseEntity<>("Quiz Created", HttpStatus.CREATED);

    }

    public ResponseEntity<List<QuestionWrapper>> getQuiz(int id) {

        Quiz quiz = quizRepository.findById(id).get();

        List<Integer> questionIds = quiz.getQuestionIds();

        ResponseEntity<List<QuestionWrapper>> questions = quizFeignClient.getQuestionsFromIds(questionIds);

        return questions;
    }

    public ResponseEntity<Integer> calculateScore(Integer id, List<Response> responses) {

        ResponseEntity<Integer> score = quizFeignClient.getQuizScore(responses);

        return score;

    }
}
