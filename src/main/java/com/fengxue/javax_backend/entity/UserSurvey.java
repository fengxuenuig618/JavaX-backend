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
@Entity(name = "user_survey")
public class UserSurvey {
    @Id
    @Column(name = "user_id")
    private int uid;


    @Column(name = "survey_question1")
    private String surveyQuestion1;

    @Column(name = "survey_question2")
    private String surveyQuestion2;

    @Column(name = "survey_question3")
    private String surveyQuestion3;

    @Column(name = "survey_question4")
    private String surveyQuestion4;

    @Column(name = "survey_question5")
    private String surveyQuestion5;

    @Column(name = "survey_question6")
    private String surveyQuestion6;

    @Column(name = "survey_question7")
    private String surveyQuestion7;

    @Column(name = "survey_question8")
    private String surveyQuestion8;

    @Column(name = "survey_question9")
    private String surveyQuestion9;

    @Column(name = "survey_question10")
    private String surveyQuestion10;

    @Column(name = "survey_question11")
    private String surveyQuestion11;

}
