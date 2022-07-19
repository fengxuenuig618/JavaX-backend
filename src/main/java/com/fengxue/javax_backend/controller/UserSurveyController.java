package com.fengxue.javax_backend.controller;

import com.fengxue.javax_backend.dao.UserDateRepository;
import com.fengxue.javax_backend.dao.UserSurveyRepository;
import com.fengxue.javax_backend.entity.UserDateTransfer;
import com.fengxue.javax_backend.entity.UserSurvey;
import com.fengxue.javax_backend.util.MyAnnotation.UserLoginToken;
import com.fengxue.javax_backend.util.Response.Response;
import com.fengxue.javax_backend.util.Response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSurveyController {
    @Autowired
    private UserSurveyRepository userSurveyRepository;

    @UserLoginToken
    @PostMapping("/postUserSurvey")
    public ResponseResult postUserDate(UserSurvey userSurvey){
//        System.out.println(userSurvey.toString());
        userSurveyRepository.save(userSurvey);
        return Response.createOkResp("survey save success");

    }


}
