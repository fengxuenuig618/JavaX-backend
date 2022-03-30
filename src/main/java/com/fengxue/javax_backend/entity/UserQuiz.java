package com.fengxue.javax_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_quiz")
public class UserQuiz {
    @Id
    @Column(name = "user_id")
    private int uid;

    @Column(name = "quiz_record")
    private String quizRecord;

    @Column(name = "done_pre_quiz")
    private String donePreQuiz;

    @Column(name = "done_quiz")
    private String doneQuiz;

    @Column(name = "done_module")
    private String doneModule;

}
