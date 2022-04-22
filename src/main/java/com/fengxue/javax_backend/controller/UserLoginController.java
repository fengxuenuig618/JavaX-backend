package com.fengxue.javax_backend.controller;



import com.fengxue.javax_backend.dao.UserAccountRepository;
import com.fengxue.javax_backend.entity.Token;
import com.fengxue.javax_backend.entity.UserAccount;
import com.fengxue.javax_backend.entity.UserLoginTransfer;
import com.fengxue.javax_backend.service.TokenService;
import com.fengxue.javax_backend.util.MyAnnotation.PassToken;
import com.fengxue.javax_backend.util.Response.*;

import com.fengxue.javax_backend.util.Standardize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserLoginController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserAccountRepository userAccountRepository;




    //校验密码
    @PassToken
    @PostMapping("/userLogin")
    public ResponseResult loginUser(UserLoginTransfer user)
    {
        if(user == null) return Response.createFailResp("null value");
        System.out.println(user.toString());
        String name = user.getUname();
        String pwd = user.getUpw();
        System.out.println("--------");
        UserAccount baseUser = userAccountRepository.findByUname(name);

        boolean ret = true;
        if(baseUser == null){
            baseUser = userAccountRepository.findByUemail(Standardize.getEmail(name));
            if(baseUser == null){
                ret = false;
            }
        }

        if(ret){
            System.out.println(baseUser.getUname());
            if(baseUser.getUpw().equals(pwd)){
                String token = tokenService.getToken(baseUser);
                Token userToken = new Token(token);
                return Response.createOkResp(String.valueOf(baseUser.getUid())+";"+baseUser.getUname(),userToken);
            }
            else{
                return Response.createFailResp("wrong password");
            }
        }
        else{
            return Response.createFailResp("no such user");
        }


    }

}
