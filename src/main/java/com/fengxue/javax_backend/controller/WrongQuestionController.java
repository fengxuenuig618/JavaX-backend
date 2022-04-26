package com.fengxue.javax_backend.controller;

import com.fengxue.javax_backend.dao.CourseMcqRepository;
import com.fengxue.javax_backend.dao.UserQuizRepository;
import com.fengxue.javax_backend.entity.ChapterTutorial;
import com.fengxue.javax_backend.entity.CourseMcq;
import com.fengxue.javax_backend.entity.UserQuiz;
import com.fengxue.javax_backend.util.DataProcess;
import com.fengxue.javax_backend.util.McqStateMachine;
import com.fengxue.javax_backend.util.MyAnnotation.UserLoginToken;
import com.fengxue.javax_backend.util.Response.Response;
import com.fengxue.javax_backend.util.Response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class WrongQuestionController {
    @Autowired
    private CourseMcqRepository courseMcqRepository;

    @Autowired
    private UserQuizRepository userQuizRepository;

    @UserLoginToken
    @GetMapping("/getWrongQuiz/{id}")
    public ResponseResult<List<CourseMcq>> selectChapterTutorial(@PathVariable(name = "id") int id)
    {

        UserQuiz userQuiz = userQuizRepository.findByUid(id);
        if(userQuiz== null) return Response.createFailResp("no wrong");
        Map<String,String> userRecord = DataProcess.getBisectMap(userQuizRepository.getById(id).getQuizRecord(),";",":");
        List<CourseMcq> retMcq = new ArrayList<>();
        for(String qid:userRecord.keySet()){
            String value = userRecord.get(qid);
            if(McqStateMachine.getWrongSet().contains(value)){
                retMcq.add(courseMcqRepository.findByMcqId(qid));
            }
        }
        for(CourseMcq quiz : retMcq){
            for(String option:DataProcess.getDelimitArray(quiz.getStrOptions(),";")){
                quiz.getOptions().add(option);
            }
        }

        if(retMcq.size()>0) return Response.createOkResp(retMcq);
        else return Response.createFailResp("no wrong");
    }
}
