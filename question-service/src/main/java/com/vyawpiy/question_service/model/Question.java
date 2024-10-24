package com.vyawpiy.question_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(name = "right_answer", nullable = false, length = 315)
    private String rightAnswer;

    @Column(nullable = false, length = 315)
    private String option1;

    @Column(nullable = false, length = 315)
    private String option2;

    @Column(length = 315)
    private String option3;

    @Column(length = 315)
    private String option4;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "difficulty_level", nullable = false, length = 20)
    private String difficultyLevel;

}
