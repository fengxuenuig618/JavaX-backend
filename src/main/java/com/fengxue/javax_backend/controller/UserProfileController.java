package com.fengxue.javax_backend.controller;

import com.fengxue.javax_backend.dao.CourseMcqRepository;
import com.fengxue.javax_backend.dao.CourseModuleRepository;
import com.fengxue.javax_backend.dao.UserProfileSettingRepository;
import com.fengxue.javax_backend.dao.UserQuizRepository;
import com.fengxue.javax_backend.entity.*;
import com.fengxue.javax_backend.service.TokenService;
import com.fengxue.javax_backend.util.DataProcess;
import com.fengxue.javax_backend.util.McqStateMachine;
import com.fengxue.javax_backend.util.MyAnnotation.UserLoginToken;
import com.fengxue.javax_backend.util.Response.Response;
import com.fengxue.javax_backend.util.Response.ResponseResult;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class UserProfileController {
    @Autowired
    private UserProfileSettingRepository userProfileSettingRepository;

    @Autowired
    private UserQuizRepository userQuizRepository;

    @Autowired
    private CourseMcqRepository courseMcqRepository;

    @Autowired
    private CourseModuleRepository courseModuleRepository;

    @UserLoginToken
    @GetMapping("/getUserProfile/{id}")
    public ResponseResult getProfile(@PathVariable(name = "id") int id){

        UserProfileSetting baseSetting = userProfileSettingRepository.findByUid(id);
        if(baseSetting==null || baseSetting.getHasSet()!=1){
            return Response.createOkResp("no set");
        }

        String[] userSelectedModule = DataProcess.getDelimitArray(baseSetting.getSelectModules(),",");

        UserProfileTransfer userProfileTransfer = new UserProfileTransfer();
        userProfileTransfer.setUid(id);
        userProfileTransfer.setUserLevel(baseSetting.getGlobalLevel());

        int allQuiz = 0;
        for(String module:userSelectedModule){
            allQuiz += courseMcqRepository.countAllByModuleBelongAndGlobalLevelLessThanEqual(module,baseSetting.getGlobalLevel());
        }



        UserQuiz userQuiz = userQuizRepository.findByUid(id);
        if(userQuiz==null){
            userProfileTransfer.setUndoneModules(Arrays.asList(userSelectedModule));
            userProfileTransfer.setOverallScore("None");
            userProfileTransfer.setOverallRight(0);
            userProfileTransfer.setOverallWrong(0);
            userProfileTransfer.setOverallNone(allQuiz);
            return Response.createOkResp(userProfileTransfer);
        }
        else{
            Map<String,String> userQuizMap = DataProcess.getBisectMap(userQuiz.getQuizRecord(),";",":");
            int allWrong = 0;
            int allRight = 0;
            for(String mcqId:userQuizMap.keySet()){
                String curState = userQuizMap.get(mcqId);
                if(McqStateMachine.getCorrectSet().contains(curState)){
                    allRight++;
                }
                else allWrong++;
            }
            userProfileTransfer.setOverallRight(allRight);
            userProfileTransfer.setOverallWrong(allWrong);
            userProfileTransfer.setOverallNone(allQuiz-allRight-allWrong);
            userProfileTransfer.setOverallScore(McqStateMachine.getScore(userQuizMap));

            Map<String,String> assessmentMap = new HashMap<>();

            String[] doneModules = DataProcess.getDelimitArray(userQuiz.getDoneModule(),";");
            for(String module:userSelectedModule){
                boolean isDone = false;
                for(String done:doneModules){
                    if (module.equals(done)) {
                        isDone = true;
                        break;
                    }
                }
                if(isDone){
                    UserModuleAssessmentTransfer curModule = getModuleAssessment(module,userQuizMap);
                    assessmentMap.put(curModule.getModuleTitle(), curModule.getModuleScore());
                    userProfileTransfer.getDoneModules().add(curModule);
                }
                else{
                    userProfileTransfer.getUndoneModules()
                            .add(courseModuleRepository.findByModuleId(module).getModuleTitle());
                }
            }
            userProfileTransfer.setOverallAssessment(McqStateMachine.getAssessment(assessmentMap,"module"));




            return Response.createOkResp(userProfileTransfer);
        }

    }

    private UserModuleAssessmentTransfer getModuleAssessment(String moduleId,Map<String,String> userQuizMap){
        UserModuleAssessmentTransfer userModuleAssessmentTransfer = new UserModuleAssessmentTransfer();
        userModuleAssessmentTransfer.setModuleTitle(courseModuleRepository.findByModuleId(moduleId).getModuleTitle());
        Map<String,Map<String,String>> chapterMcqMap = new HashMap<>();

        Map<String,String> moduleMcqMap = new HashMap<>();
        int allRight = 0;
        int allWrong = 0;
        for(String mcqId:userQuizMap.keySet()){
            String curState = userQuizMap.get(mcqId);
            CourseMcq baseMcq = courseMcqRepository.findByMcqId(mcqId);
            if(baseMcq.getModuleBelong().equals(moduleId)){
                moduleMcqMap.put(mcqId,curState);
                if(McqStateMachine.getCorrectSet().contains(curState)){
                    allRight++;
                }
                else allWrong++;

                String chapterTitle = baseMcq.getChapterTitle();
                if(!chapterMcqMap.containsKey(chapterTitle)){
                    chapterMcqMap.put(chapterTitle,new HashMap<>());
                }
                chapterMcqMap.get(chapterTitle).put(mcqId,curState);
            }
        }
        userModuleAssessmentTransfer.setModuleScore(McqStateMachine.getScore(moduleMcqMap));
        userModuleAssessmentTransfer.setQuizWrong(allWrong);
        userModuleAssessmentTransfer.setQuizRight(allRight);

        Map<String,String> chapterScore = new HashMap<>();
        for(String chapter:chapterMcqMap.keySet()){
            chapterScore.put(chapter,McqStateMachine.getScore(chapterMcqMap.get(chapter)));
        }
        userModuleAssessmentTransfer.setModuleAssessment(McqStateMachine.getAssessment(chapterScore,"chapter"));
        return userModuleAssessmentTransfer;
    }
}
