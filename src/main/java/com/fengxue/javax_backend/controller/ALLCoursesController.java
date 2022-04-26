package com.fengxue.javax_backend.controller;

import com.fengxue.javax_backend.dao.ChapterTutorialRepository;
import com.fengxue.javax_backend.dao.CourseMcqRepository;
import com.fengxue.javax_backend.dao.CourseModuleRepository;
import com.fengxue.javax_backend.dao.UserQuizRepository;
import com.fengxue.javax_backend.entity.*;
import com.fengxue.javax_backend.util.DataProcess;
import com.fengxue.javax_backend.util.MyAnnotation.UserLoginToken;
import com.fengxue.javax_backend.util.Response.Response;
import com.fengxue.javax_backend.util.Response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ALLCoursesController {
    @Autowired
    private ChapterTutorialRepository chapterTutorialRepository;

    @Autowired
    private CourseMcqRepository courseMcqRepository;

    @Autowired
    private UserQuizRepository userQuizRepository;


    @UserLoginToken
    @GetMapping("/getAllModulesTutorial/{id}")
    public ResponseResult<List<ChapterTutorial>> selectChapterTutorial(@PathVariable(name = "id") String id)
    {
        List<ChapterTutorial> baseTutorials = chapterTutorialRepository.findAllByChapterBelong(id);

        return Response.createOkResp(baseTutorials);
    }

    @UserLoginToken
    @GetMapping("/getAllModulesQuiz/{id}")
    public ResponseResult<List<CourseMcq>> selectChapterQuiz(@PathVariable(name = "id") String chapterId)
    {
        List<CourseMcq> baseQuiz = courseMcqRepository.findAllByChapterBelong(chapterId);

        for(CourseMcq quiz : baseQuiz){
            for(String option:DataProcess.getDelimitArray(quiz.getStrOptions(),";")){
                quiz.getOptions().add(option);
            }
        }

        return Response.createOkResp(baseQuiz);
    }


}
