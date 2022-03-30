package com.fengxue.javax_backend.controller;



import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fengxue.javax_backend.dao.UserAccountRepository;
import com.fengxue.javax_backend.dao.UserLoginRepository;
import com.fengxue.javax_backend.entity.Token;
import com.fengxue.javax_backend.entity.UserAccount;
import com.fengxue.javax_backend.entity.UserLogin;
import com.fengxue.javax_backend.service.TokenService;
import com.fengxue.javax_backend.util.MyAnnotation.PassToken;
import com.fengxue.javax_backend.util.MyAnnotation.UserLoginToken;
import com.fengxue.javax_backend.util.Response.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class UserLoginController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private UserLoginRepository userLoginRepository;



    //校验密码
    @PassToken
    @PostMapping("/userLogin")
    public ResponseResult loginUser(UserLogin user)
    {
        if(user == null) return Response.createFailResp("null value");
        System.out.println(user.toString());
        String name = user.getUname();
        String pwd = user.getUpw();
        System.out.println("--------");
        UserLogin baseUser = userLoginRepository.findByUname(name);


        if(baseUser == null) return Response.createFailResp("no such user");
        else{
            System.out.println(baseUser.getUname());
            if(baseUser.getUpw().equals(pwd)){
                String token = tokenService.getToken(user);
                Token userToken = new Token(token);
                return Response.createOkResp(String.valueOf(baseUser.getUid()),userToken);
            }
            else{
                return Response.createFailResp("wrong password");
            }
        }


    }

}
