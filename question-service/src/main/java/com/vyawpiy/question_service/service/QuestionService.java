package com.vyawpiy.question_service.service;

import com.vyawpiy.question_service.model.Question;
import com.vyawpiy.question_service.model.QuestionWrapper;
import com.vyawpiy.question_service.model.Response;
import com.vyawpiy.question_service.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionRepository.findByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Question> addQuestion(Question question) {
        try {
            return new ResponseEntity<>(questionRepository.save(question), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Question(), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deleteQuestion(int id) {
        try {
            if(questionRepository.existsById(id)) {
                questionRepository.deleteById(id);
                Question question = questionRepository.findById(id).orElse(new Question());
                if (question.getId() == id) return new ResponseEntity<>("Not Deleted", HttpStatus.NOT_IMPLEMENTED);
                return new ResponseEntity<>("Deleted", HttpStatus.OK);
            }
            return new ResponseEntity<>("Question not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Error in deleting", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Question> updateQuestion(int id, Question question) {
        try {
            question.setId(id);
            Question question1 = questionRepository.save(question);
            return new ResponseEntity<>(question1, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Question(), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
        List<Integer> questionIds = questionRepository.findRandomQuestionsByCategory(categoryName, numQuestions);
        return new ResponseEntity<>(questionIds, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromIds(List<Integer> questionIds) {
        List<QuestionWrapper> wrappers = new ArrayList<>();
        List<Question> questions = new ArrayList<>();

        for(Integer id: questionIds) {
            questions.add(questionRepository.findById(id).get());
        }

        for(Question q: questions) {
            QuestionWrapper qw = new QuestionWrapper(
                    q.getId(),
                    q.getTitle(),
                    q.getOption1(),
                    q.getOption2(),
                    q.getOption3(),
                    q.getOption4()
            );

            wrappers.add(qw);
        }

        return new ResponseEntity<>(wrappers, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getQuizScore(List<Response> responses) {
        int score = 0;

        for(Response response: responses) {
            Question question = questionRepository.findById(response.getId()).get();
            if(response.getResponse().equals(question.getRightAnswer())) score++;
        }

        return new ResponseEntity<>(score, HttpStatus.OK);

    }
}
