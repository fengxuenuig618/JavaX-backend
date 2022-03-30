package com.fengxue.javax_backend.controller;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fengxue.javax_backend.dao.UserAccountRepository;
import com.fengxue.javax_backend.dao.UserLoginRepository;
import com.fengxue.javax_backend.dao.UserProfileSettingRepository;
import com.fengxue.javax_backend.entity.Token;
import com.fengxue.javax_backend.entity.UserAccount;
import com.fengxue.javax_backend.entity.UserLogin;
import com.fengxue.javax_backend.entity.UserProfileSetting;
import com.fengxue.javax_backend.service.TokenService;
import com.fengxue.javax_backend.util.MyAnnotation.PassToken;
import com.fengxue.javax_backend.util.MyAnnotation.UserLoginToken;
import com.fengxue.javax_backend.util.Response.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class UserRegisterController {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private UserProfileSettingRepository userProfileSettingRepository;

    //添加
    @PassToken
    @PostMapping("/userRegister")
    public ResponseResult registerUser(UserAccount user)
    {

        String name = user.getUname();
        UserAccount baseUser = userAccountRepository.findByUname(name);
        if(baseUser != null){
            return Response.createFailResp("duplicate username");
        }
        else{
            userAccountRepository.save(user);
            userLoginRepository.save(new UserLogin(user.getUid(),user.getUname(),user.getUpw()));
            userProfileSettingRepository.save(
                    new UserProfileSetting(user.getUid(),0,0,0,0,0,0,"",new HashMap<String,Boolean>()));
            return Response.createOkResp("register success");
        }

    }



}
