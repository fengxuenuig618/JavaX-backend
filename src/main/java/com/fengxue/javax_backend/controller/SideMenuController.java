package com.fengxue.javax_backend.controller;

import com.fengxue.javax_backend.dao.CourseChapterRepository;
import com.fengxue.javax_backend.dao.CourseModuleRepository;
import com.fengxue.javax_backend.dao.UserAccountRepository;
import com.fengxue.javax_backend.dao.UserProfileSettingRepository;
import com.fengxue.javax_backend.entity.*;
import com.fengxue.javax_backend.util.MyAnnotation.UserLoginToken;
import com.fengxue.javax_backend.util.Response.Response;
import com.fengxue.javax_backend.util.Response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class SideMenuController {

    @Autowired
    private CourseModuleRepository courseModuleRepository;
    @Autowired
    private CourseChapterRepository courseChapterRepository;
    @Autowired
    private UserProfileSettingRepository userProfileSettingRepository;

    //查询全部module项菜单
    @UserLoginToken
    @GetMapping("/menuAllModules")
    public ResponseResult<List<MenuCourseModule>> selectAllModules()
    {
        List<MenuCourseModule> moduleList = new LinkedList<>();
        List<CourseModule> baseModules = courseModuleRepository.findAll();
        for(CourseModule module:baseModules){
            String moduleId = module.getModuleId();
            List<CourseChapter> baseChapters = courseChapterRepository.findAllByModuleBelong(moduleId);
            List<MenuCourseChapter> chapterList = new LinkedList<>();
            for(CourseChapter chapter:baseChapters){
                chapterList.add(new MenuCourseChapter(chapter.getChapterId(),chapter.getChapterTitle()));
            }
            moduleList.add(new MenuCourseModule(module.getModuleId(),module.getModuleTitle(),chapterList));
        }

        return Response.createOkResp(moduleList);
    }

    //查询用户定制module菜单
    @UserLoginToken
    @GetMapping("/menuMyModules/{id}")
    public ResponseResult<List<MenuCourseModule>> selectMyModules(@PathVariable(name = "id") int id)
    {
        //获取用户设置
        UserProfileSetting userSetting = userProfileSettingRepository.findByUid(id);
        int globalLevel = userSetting.getGlobalLevel(); //用户等级
        String selectedModules = userSetting.getSelectModules();
        String[] temp;
        String delimeter = ",";  // 指定分割字符
        temp = selectedModules.split(delimeter); // 分割字符串
        Set<String> userModuleSet = new HashSet<>(Arrays.asList(temp)); //用户模块

        List<MenuCourseModule> moduleList = new LinkedList<>();
        List<CourseModule> baseModules = courseModuleRepository.findAll();
        for(CourseModule module:baseModules){
            //是否选中该模块
            if(userModuleSet.contains(module.getModuleId())){
                String moduleId = module.getModuleId();
                List<CourseChapter> baseChapters = courseChapterRepository.findAllByModuleBelong(moduleId);
                List<MenuCourseChapter> chapterList = new LinkedList<>();
                for(CourseChapter chapter:baseChapters){
                    if(chapter.getGlobalLevel()<=globalLevel){
                        chapterList.add(new MenuCourseChapter(chapter.getChapterId(),chapter.getChapterTitle()));
                    }
                }
                moduleList.add(new MenuCourseModule(module.getModuleId(),module.getModuleTitle(),chapterList));
            }

        }

        return Response.createOkResp(moduleList);
    }

//    @GetMapping("/allChapters")
//    public ResponseResult<List<CourseChapter>> selectAllChapters()
//    {
//
//        List<CourseChapter> baseModules = courseChapterRepository.findAll();
//
//        return Response.createOkResp(baseModules);
//    }
}
