package com.fengxue.javax_backend.controller;

import com.fengxue.javax_backend.dao.UserQuizRepository;
import com.fengxue.javax_backend.entity.UserQuiz;
import com.fengxue.javax_backend.entity.UserQuizTransfer;
import com.fengxue.javax_backend.util.DataProcess;
import com.fengxue.javax_backend.util.McqStateMachine;
import com.fengxue.javax_backend.util.MyAnnotation.UserLoginToken;
import com.fengxue.javax_backend.util.Response.Response;
import com.fengxue.javax_backend.util.Response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
public class McqSaveController {
    @Autowired
    private UserQuizRepository userQuizRepository;

    @UserLoginToken
    @PostMapping("/savePreQuiz/{id}:{chapter}")
    public ResponseResult savePreQuiz(@PathVariable(name = "id") int userId,
                                      @PathVariable(name = "chapter") String chapter,
                                      UserQuizTransfer userQuiz)
    {
        String[] userWrongArray = DataProcess.getDelimitArray(userQuiz.getWrongQuiz(),";");
        String[] userCorrectArray = DataProcess.getDelimitArray(userQuiz.getCorrectQuiz(),";");
        UserQuiz baseUser = userQuizRepository.findByUid(userId);
        if(baseUser==null){
            UserQuiz newUserQuiz = new UserQuiz();
            newUserQuiz.setUid(userId);
            StringBuilder saveWrong = new StringBuilder();
            StringBuilder saveRight = new StringBuilder();
            for(String wrong:userWrongArray){
                if(wrong.equals("")) continue;
                saveWrong.append(wrong);
                saveWrong.append(":");
                saveWrong.append(McqStateMachine.PreWrong.getValue());
                saveWrong.append(";");
            }
            for(String right:userCorrectArray){
                if(right.equals("")) continue;
                saveRight.append(right);
                saveRight.append(":");
                saveRight.append(McqStateMachine.PreRight.getValue());
                saveRight.append(";");
            }
            String quizRecordSave = saveRight.toString()+saveWrong.toString();
            newUserQuiz.setQuizRecord(quizRecordSave);

            String saveChapter = "";
            saveChapter += chapter;
            saveChapter += ";";
            newUserQuiz.setDonePreQuiz(saveChapter);

            userQuizRepository.save(newUserQuiz);
            return Response.createOkResp();
        }
        else{
            Map<String,String> baseQuizMap = DataProcess.getBisectMap(baseUser.getQuizRecord(),";",":");
            for(String userWrong:userWrongArray){
                if(userWrong.equals("")) continue;
                if(baseQuizMap.containsKey(userWrong)){
                    String curMcqState = baseQuizMap.get(userWrong);
                    String nextMcqState = McqStateMachine.PreWrong.getValue();
                    baseQuizMap.put(userWrong,nextMcqState);
                }
                else{
                    baseQuizMap.put(userWrong,McqStateMachine.PreWrong.getValue());
                }
            }
            for(String userRight:userCorrectArray){
                if(userRight.equals("")) continue;
                if(baseQuizMap.containsKey(userRight)){
                    String curMcqState = baseQuizMap.get(userRight);
                    String nextMcqState = McqStateMachine.PreRight.getValue();
                    baseQuizMap.put(userRight,nextMcqState);
                }
                else{
                    baseQuizMap.put(userRight,McqStateMachine.PreRight.getValue());
                }
            }
            StringBuilder quizRecordSave = new StringBuilder();
            for(String key:baseQuizMap.keySet()){
                quizRecordSave.append(key);
                quizRecordSave.append(":");
                quizRecordSave.append(baseQuizMap.get(key));
                quizRecordSave.append(";");
            }
            baseUser.setQuizRecord(quizRecordSave.toString());

            Set<String> donePreQuizSet = DataProcess.getDelimitSet(baseUser.getDonePreQuiz(),";");
            if(!donePreQuizSet.contains(chapter)){
                baseUser.setDonePreQuiz(baseUser.getDonePreQuiz()+chapter+";");
            }
            userQuizRepository.save(baseUser);
            return Response.createOkResp();
        }

    }

    @UserLoginToken
    @PostMapping("/saveAfterQuiz/{id}:{chapter}")
    public ResponseResult saveAfterQuiz(@PathVariable(name = "id") int userId,
                                      @PathVariable(name = "chapter") String chapter,
                                      UserQuizTransfer userQuiz)
    {
        String[] userWrongArray = DataProcess.getDelimitArray(userQuiz.getWrongQuiz(),";");
        String[] userCorrectArray = DataProcess.getDelimitArray(userQuiz.getCorrectQuiz(),";");
        UserQuiz baseUser = userQuizRepository.findByUid(userId);
        Map<String,String> baseQuizMap = DataProcess.getBisectMap(baseUser.getQuizRecord(),";",":");
        for(String userWrong:userWrongArray){
            if(userWrong.equals("")) continue;
            if(baseQuizMap.containsKey(userWrong)){
                String curMcqState = baseQuizMap.get(userWrong);
                String nextMcqState = McqStateMachine.getNextWrong(curMcqState);
                baseQuizMap.put(userWrong,nextMcqState);
            }
            else{
                baseQuizMap.put(userWrong,McqStateMachine.LearnWrong.getValue());
            }
        }
        for(String userRight:userCorrectArray){
            if(userRight.equals("")) continue;
            if(baseQuizMap.containsKey(userRight)){
                String curMcqState = baseQuizMap.get(userRight);
                String nextMcqState = McqStateMachine.getNextRight(curMcqState);
                baseQuizMap.put(userRight,nextMcqState);
            }
            else{
                baseQuizMap.put(userRight,McqStateMachine.OccasionalRight.getValue());
            }
        }
        StringBuilder quizRecordSave = new StringBuilder();
        for(String key:baseQuizMap.keySet()){
            quizRecordSave.append(key);
            quizRecordSave.append(":");
            quizRecordSave.append(baseQuizMap.get(key));
            quizRecordSave.append(";");
        }
        baseUser.setQuizRecord(quizRecordSave.toString());

        Set<String> doneQuizSet = DataProcess.getDelimitSet(baseUser.getDoneQuiz(),";");
        if(!doneQuizSet.contains(chapter)){
            if(doneQuizSet.size()==0){
                baseUser.setDoneQuiz(chapter+";");
            }
            else{
                baseUser.setDoneQuiz(baseUser.getDoneQuiz()+chapter+";");
            }
        }
        userQuizRepository.save(baseUser);
        return Response.createOkResp();
    }

    @UserLoginToken
    @PostMapping("/saveModuleQuiz/{id}:{module}")
    public ResponseResult saveModuleQuiz(@PathVariable(name = "id") int userId,
                                        @PathVariable(name = "module") String module,
                                        UserQuizTransfer userQuiz)
    {
        String[] userWrongArray = DataProcess.getDelimitArray(userQuiz.getWrongQuiz(),";");
        String[] userCorrectArray = DataProcess.getDelimitArray(userQuiz.getCorrectQuiz(),";");
        UserQuiz baseUser = userQuizRepository.findByUid(userId);
        Map<String,String> baseQuizMap = DataProcess.getBisectMap(baseUser.getQuizRecord(),";",":");
        for(String userWrong:userWrongArray){
            if(userWrong.equals("")) continue;
            if(baseQuizMap.containsKey(userWrong)){
                String curMcqState = baseQuizMap.get(userWrong);
                String nextMcqState = McqStateMachine.getNextWrong(curMcqState);
                baseQuizMap.put(userWrong,nextMcqState);
            }
            else{
                baseQuizMap.put(userWrong,McqStateMachine.LearnWrong.getValue());
            }
        }
        for(String userRight:userCorrectArray){
            if(userRight.equals("")) continue;
            if(baseQuizMap.containsKey(userRight)){
                String curMcqState = baseQuizMap.get(userRight);
                String nextMcqState = McqStateMachine.getNextRight(curMcqState);
                baseQuizMap.put(userRight,nextMcqState);
            }
            else{
                baseQuizMap.put(userRight,McqStateMachine.OccasionalRight.getValue());
            }
        }
        StringBuilder quizRecordSave = new StringBuilder();
        for(String key:baseQuizMap.keySet()){
            quizRecordSave.append(key);
            quizRecordSave.append(":");
            quizRecordSave.append(baseQuizMap.get(key));
            quizRecordSave.append(";");
        }
        baseUser.setQuizRecord(quizRecordSave.toString());

        Set<String> doneQuizSet = DataProcess.getDelimitSet(baseUser.getDoneModule(),";");
        if(!doneQuizSet.contains(module)){
            if(doneQuizSet.size()==0){
                baseUser.setDoneModule(module+";");
            }
            else{
                baseUser.setDoneModule(baseUser.getDoneModule()+module+";");
            }
        }
        userQuizRepository.save(baseUser);
        return Response.createOkResp();
    }


    @UserLoginToken
    @PostMapping("/saveWrongQuiz/{id}")
    public ResponseResult saveWrongQuiz(@PathVariable(name = "id") int userId,
                                   UserQuizTransfer userQuiz)
    {
        String[] userWrongArray = DataProcess.getDelimitArray(userQuiz.getWrongQuiz(),";");
        String[] userCorrectArray = DataProcess.getDelimitArray(userQuiz.getCorrectQuiz(),";");
        UserQuiz baseUser = userQuizRepository.findByUid(userId);
        Map<String,String> baseQuizMap = DataProcess.getBisectMap(baseUser.getQuizRecord(),";",":");
        for(String userWrong:userWrongArray){
            if(userWrong.equals("")) continue;
            if(baseQuizMap.containsKey(userWrong)){
                String curMcqState = baseQuizMap.get(userWrong);
                String nextMcqState = McqStateMachine.getNextWrong(curMcqState);
                baseQuizMap.put(userWrong,nextMcqState);
            }
            else{
                baseQuizMap.put(userWrong,McqStateMachine.LearnWrong.getValue());
            }
        }
        for(String userRight:userCorrectArray){
            if(userRight.equals("")) continue;
            if(baseQuizMap.containsKey(userRight)){
                String curMcqState = baseQuizMap.get(userRight);
                String nextMcqState = McqStateMachine.getNextRight(curMcqState);
                baseQuizMap.put(userRight,nextMcqState);
            }
            else{
                baseQuizMap.put(userRight,McqStateMachine.OccasionalRight.getValue());
            }
        }
        StringBuilder quizRecordSave = new StringBuilder();
        for(String key:baseQuizMap.keySet()){
            quizRecordSave.append(key);
            quizRecordSave.append(":");
            quizRecordSave.append(baseQuizMap.get(key));
            quizRecordSave.append(";");
        }
        baseUser.setQuizRecord(quizRecordSave.toString());
        userQuizRepository.save(baseUser);
        return Response.createOkResp();
    }
}
