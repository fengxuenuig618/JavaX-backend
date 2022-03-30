package com.fengxue.javax_backend.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
public class UserProfileSettingController {

    @Autowired
    private UserProfileSettingRepository userProfileSetting;
    @Autowired
    private CourseModuleRepository courseModuleRepository;


    //@UserLoginToken
    @GetMapping("/getUserProfileSetting/{id}")
    public ResponseResult getAccount(@PathVariable(name = "id") int id){

        //找到全部章节的id号
        List<CourseModule> baseModules = courseModuleRepository.findAll();

        UserProfileSetting baseUser = userProfileSetting.findByUid(id);
        String selected = baseUser.getSelectModules();
        String[] temp;
        String delimeter = ",";  // 指定分割字符
        temp = selected.split(delimeter); // 分割字符串

        Set<String> userModuleSet = new HashSet<>(Arrays.asList(temp));

        for(CourseModule module:baseModules){
            if(userModuleSet.contains(module.getModuleId())){
                baseUser.getModulesMap().put(module.getModuleId(),true);
            }
            else baseUser.getModulesMap().put(module.getModuleId(),false);
        }

        return Response.createOkResp(baseUser);
    }

    //更新信息
    @UserLoginToken
    @PostMapping("/updateUserProfileSetting")
    public ResponseResult updateAccount(UserProfileSetting user){
        System.out.println(user.toString());
        if(user == null) return Response.createFailResp("null value");
        userProfileSetting.save(user);
        return Response.createOkResp(user);

    }
}
