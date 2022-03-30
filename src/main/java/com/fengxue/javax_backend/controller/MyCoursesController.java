package com.fengxue.javax_backend.controller;

import com.fengxue.javax_backend.dao.ChapterTutorialRepository;
import com.fengxue.javax_backend.dao.CourseMcqRepository;
import com.fengxue.javax_backend.dao.UserQuizRepository;
import com.fengxue.javax_backend.entity.ChapterTutorial;
import com.fengxue.javax_backend.entity.CourseMcq;
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


    @GetMapping("/getMyModulesPreQuiz/{uid}:{chapterId}:{level}")
    public ResponseResult<List<CourseMcq>> selectPreQuiz(@PathVariable(name = "chapterId") String chapterId,
                                                             @PathVariable(name = "level") int level,
                                                             @PathVariable(name = "uid") int uid)
    {
        List<CourseMcq> baseQuiz = courseMcqRepository.findAllByChapterBelongAndGlobalLevelLessThanEqual(chapterId,level);
        Map<String,List<CourseMcq>> mcqMap = new HashMap<>();
        for(CourseMcq quiz : baseQuiz){
            for(String option:DataProcess.getDelimitArray(quiz.getStrOptions(),";")){
                quiz.getOptions().add(option);
            }
            String curTutorial = quiz.getParagraphBelong();
            if(!mcqMap.containsKey(curTutorial)){
                mcqMap.put(curTutorial,new ArrayList<CourseMcq>());
            }
            mcqMap.get(curTutorial).add(quiz);
        }
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
    @GetMapping("/getMyModulesTutorial/{chapterId}:{level}")
    public ResponseResult<List<ChapterTutorial>> selectChapterTutorial(@PathVariable(name = "level") int level,
                                                                       @PathVariable(name = "chapterId") String chapterId)
    {
        List<ChapterTutorial> baseTutorials =
                chapterTutorialRepository.findAllByChapterBelongAndGlobalLevelLessThanEqual(chapterId,level);

        return Response.createOkResp(baseTutorials);
    }

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
