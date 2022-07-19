package com.fengxue.javax_backend.controller;

import com.fengxue.javax_backend.dao.*;
import com.fengxue.javax_backend.entity.*;
import com.fengxue.javax_backend.util.DataProcess;
import com.fengxue.javax_backend.util.McqStateMachine;
import com.fengxue.javax_backend.util.MyAnnotation.UserLoginToken;
import com.fengxue.javax_backend.util.Response.Response;
import com.fengxue.javax_backend.util.Response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class MyCoursesController {

    final int MIN_MCQ = 1;
    final double PRE_MCQ_PROPORTION = 0.3;

    @Autowired
    private ChapterTutorialRepository chapterTutorialRepository;

    @Autowired
    private CourseMcqRepository courseMcqRepository;

    @Autowired
    private UserQuizRepository userQuizRepository;

    @Autowired
    private UserProfileSettingRepository userProfileSettingRepository;

    @Autowired
    private CourseChapterRepository courseChapterRepository;

    @Autowired
    private UserSkipRepository userSkipRepository;


    @UserLoginToken
    @GetMapping("/getMyModulesPreQuiz/{uid}:{chapterId}:{level}")
    public ResponseResult<List<CourseMcq>> selectPreQuiz(@PathVariable(name = "chapterId") String chapterId,
                                                             @PathVariable(name = "level") int level,
                                                             @PathVariable(name = "uid") int uid)
    {
        //所有满足等级要求的该章节题目
        List<CourseMcq> baseQuiz = courseMcqRepository.findAllByChapterBelongAndGlobalLevelLessThanEqual(chapterId,level);

        //Map<章节id,该章节题目组>
        Map<String,List<CourseMcq>> mcqMap = new HashMap<>();
        for(CourseMcq quiz : baseQuiz){
            //填充选项
            for(String option:DataProcess.getDelimitArray(quiz.getStrOptions(),";")){
                quiz.getOptions().add(option);
            }
            String curTutorial = quiz.getParagraphBelong();
            if(!mcqMap.containsKey(curTutorial)){
                mcqMap.put(curTutorial,new ArrayList<CourseMcq>());
            }
            mcqMap.get(curTutorial).add(quiz);
        }

        //返回题目组
        List<CourseMcq> retQuiz = new ArrayList<>();
        for(String key:mcqMap.keySet()){
            List<CourseMcq> curParagraph = mcqMap.get(key);
            int min = (int) (curParagraph.size() * PRE_MCQ_PROPORTION);
            min = Math.max(MIN_MCQ, min);
            Random r = new Random();
            Set<Integer> randomSet= new HashSet<>();
            if(min>= curParagraph.size()){
                retQuiz.addAll(curParagraph);
            }
            else{
                while(randomSet.size()<min){
                    randomSet.add(r.nextInt(curParagraph.size()-1));
                }
                for(int index:randomSet){
                    retQuiz.add(curParagraph.get(index));
                }
            }

        }
        return Response.createOkResp(retQuiz);

    }


    @UserLoginToken
    @GetMapping("/getMyModulesTutorial/{uid}:{chapterId}:{level}")
    public ResponseResult<List<ChapterTutorial>> selectChapterTutorial(@PathVariable(name = "level") int level,
                                                                       @PathVariable(name = "chapterId") String chapterId,
                                                                       @PathVariable(name = "uid") int uid)
    {
        List<ChapterTutorial> baseTutorials =
                chapterTutorialRepository.findAllByChapterBelongAndGlobalLevelLessThanEqual(chapterId,level);
        Set<String> skipSet = DataProcess.getDelimitSet(userSkipRepository.findByUid(uid).getSkipTutorial(),";") ;
        baseTutorials.removeIf(chapterTutorial -> skipSet.contains(chapterTutorial.getTutorialId()));

        return Response.createOkResp(baseTutorials);
    }

    @UserLoginToken
    @GetMapping("/saveTutorial/{uid}:{chapterId}:{skips}")
    public ResponseResult<String> saveTutorial(@PathVariable(name = "chapterId") String chapterId,
                                                             @PathVariable(name = "skips") String skips,
                                                             @PathVariable(name = "uid") int uid)
    {
        System.out.println("str*************  "+skips);
        List<ChapterTutorial> tutorials = chapterTutorialRepository.findAllByChapterBelong(chapterId);
        Set<String> skipSet = DataProcess.getDelimitSet(skips,"-");
        Set<String> userSkip = new HashSet<>();
        Set<String> userNotSkip = new HashSet<>();
        String userSkipStr = "";
        for(ChapterTutorial chapterTutorial:tutorials){
            if(skipSet.contains(chapterTutorial.getTutorialTitle())){
                userSkip.add(chapterTutorial.getTutorialId());
                userSkipStr += chapterTutorial.getTutorialId();
                userSkipStr += ";";
            }
            else{
                userNotSkip.add(chapterTutorial.getTutorialId());
            }
        }
        System.out.println("not skip -- "+userNotSkip.toString());
        System.out.println("skip -- "+userSkip.toString());
        UserSkip userData = userSkipRepository.findByUid(uid);
        if(userData == null){
            userData = new UserSkip();
            userData.setUid(uid);
            userData.setSkipTutorial(userSkipStr);
            userSkipRepository.save(userData);
        }
        else{
            Set<String> dataSet = DataProcess.getDelimitSet(userData.getSkipTutorial(),";");
            dataSet.addAll(userSkip);
            dataSet.removeAll(userNotSkip);
            userData.setSkipTutorial(DataProcess.set2String(dataSet,";"));
            userSkipRepository.save(userData);
        }
        return Response.createOkResp();
    }


    @UserLoginToken
    @GetMapping("/getMyModulesQuiz/{uid}:{chapterId}:{level}")
    public ResponseResult<List<CourseMcq>> selectChapterQuiz(@PathVariable(name = "chapterId") String chapterId,
                                                             @PathVariable(name = "level") int level,
                                                             @PathVariable(name = "uid") int uid)
    {
        List<CourseMcq> baseQuiz = courseMcqRepository.findAllByChapterBelongAndGlobalLevelLessThanEqual(chapterId,level);
        for(CourseMcq quiz : baseQuiz){
            for(String option:DataProcess.getDelimitArray(quiz.getStrOptions(),";")){
                quiz.getOptions().add(option);
            }
        }
        return Response.createOkResp(baseQuiz);

    }

    @UserLoginToken
    @GetMapping("/getModuleQuiz/{uid}:{moduleId}")
    public ResponseResult<List<CourseMcq>> getModuleQuiz(@PathVariable(name = "moduleId") String moduleId,
                                                             @PathVariable(name = "uid") int uid)
    {
        Map<String,String> userRecord = DataProcess.getBisectMap(userQuizRepository.getById(uid).getQuizRecord(),";",":");
        List<CourseMcq> retMcq = new ArrayList<>();
        for(String qid:userRecord.keySet()){
            String value = userRecord.get(qid);
            if(McqStateMachine.getUncertainSet().contains(value)){
                CourseMcq curMcq = courseMcqRepository.findByMcqId(qid);
                if(curMcq.getModuleBelong().equals(moduleId)){
                    retMcq.add(curMcq);
                }
            }
        }
        for(CourseMcq quiz : retMcq){
            for(String option:DataProcess.getDelimitArray(quiz.getStrOptions(),";")){
                quiz.getOptions().add(option);
            }
        }

        if(retMcq.size()>0) return Response.createOkResp(retMcq);
        else return Response.createFailResp("no quiz");
    }

    @UserLoginToken
    @GetMapping("/checkModuleQuiz/{uid}:{moduleId}")
    public ResponseResult<List<CourseMcq>> checkModuleQuiz(@PathVariable(name = "uid") int uid,
                                                        @PathVariable(name = "moduleId") String moduleId) {
        UserProfileSetting userProfileSetting = userProfileSettingRepository.findByUid(uid);
        int level = userProfileSetting.getGlobalLevel();
        List<CourseChapter> chapters =  courseChapterRepository.findAllByModuleBelong(moduleId);
        List<String> chapterIds = new ArrayList<>();
        for(CourseChapter courseChapter:chapters){
            if(courseChapter.getGlobalLevel() <= level)  chapterIds.add(courseChapter.getChapterId());
        }
        Set<String> doneChapters = DataProcess.getDelimitSet(userQuizRepository.getById(uid).getDoneQuiz(),";");
        for(String curChapter:chapterIds){
            if(!doneChapters.contains(curChapter))  return Response.createFailResp("no");
        }



        return Response.createOkResp("yes");


    }

    @UserLoginToken
    @GetMapping("/checkFinalQuiz/{uid}")
    public ResponseResult<List<CourseMcq>> checkFinalQuiz(@PathVariable(name = "uid") int uid) {
        UserProfileSetting userProfileSetting = userProfileSettingRepository.findByUid(uid);
        String[] modules = DataProcess.getDelimitArray(userProfileSetting.getSelectModules(),",");
        Set<String> doneModules = DataProcess.getDelimitSet(userQuizRepository.getById(uid).getDoneModule(),";");
        for(String curModule:modules){
            if(!doneModules.contains(curModule))  return Response.createFailResp("no");
        }
        return Response.createOkResp("yes");
    }

    @UserLoginToken
    @GetMapping("/getFinalQuiz/{uid}")
    public ResponseResult<List<CourseMcq>> getFinalQuiz(@PathVariable(name = "uid") int uid)
    {
        Map<String,String> userRecord = DataProcess.getBisectMap(userQuizRepository.getById(uid).getQuizRecord(),";",":");
        List<CourseMcq> retMcq = new ArrayList<>();
        for(String qid:userRecord.keySet()){
            String value = userRecord.get(qid);
            if(McqStateMachine.getUncertainSet().contains(value)){
                retMcq.add(courseMcqRepository.findByMcqId(qid));
            }
        }
        for(CourseMcq quiz : retMcq){
            for(String option:DataProcess.getDelimitArray(quiz.getStrOptions(),";")){
                quiz.getOptions().add(option);
            }
        }

        if(retMcq.size()>0) return Response.createOkResp(retMcq);
        else return Response.createFailResp("no quiz");
    }
}
