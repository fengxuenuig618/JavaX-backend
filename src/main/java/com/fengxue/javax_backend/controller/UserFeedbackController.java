package com.fengxue.javax_backend.controller;

import com.fengxue.javax_backend.dao.BugReportRepository;
import com.fengxue.javax_backend.dao.UserFeedbackRepository;
import com.fengxue.javax_backend.entity.BugReport;
import com.fengxue.javax_backend.entity.UserFeedback;
import com.fengxue.javax_backend.util.MyAnnotation.UserLoginToken;
import com.fengxue.javax_backend.util.Response.Response;
import com.fengxue.javax_backend.util.Response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserFeedbackController {

    @Autowired
    private UserFeedbackRepository userFeedbackRepository;

    @Autowired
    private BugReportRepository bugReportRepository;

    @UserLoginToken
    @PostMapping("/feedback/rating&review")
    public ResponseResult postFeedback(UserFeedback feedback)
    {
            userFeedbackRepository.save(feedback);
        return Response.createOkResp();
    }

    @UserLoginToken
    @PostMapping("/feedback/reportBug")
    public ResponseResult postBugReport(BugReport bugReport)
    {
            bugReportRepository.save(bugReport);
        return Response.createOkResp();
    }

}
