package com.fengxue.javax_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModuleAssessmentTransfer {

    private String moduleTitle;

    //用户总体评分
    private String moduleScore;

    //总体答题
    private int quizRight;
    private int quizWrong;

    //总体评价
    private List<String> moduleAssessment = new ArrayList<>();
}
