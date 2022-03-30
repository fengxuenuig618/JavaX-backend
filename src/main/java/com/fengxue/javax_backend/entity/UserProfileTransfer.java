package com.fengxue.javax_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileTransfer {

    private int uid;

    //用户设定等级
    private int userLevel;

    //用户总体评分
    private String overallScore;

    //总体答题
    private int overallRight;
    private int overallWrong;
    private int overallNone;

    //总体评价
    private List<String> overallAssessment = new ArrayList<>();

    //已完成章节
    private List<UserModuleAssessmentTransfer> doneModules = new ArrayList<>();

    //未完成章节
    private List<String> undoneModules = new ArrayList<>();






}
