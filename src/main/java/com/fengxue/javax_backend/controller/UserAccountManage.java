package com.fengxue.javax_backend.controller;


import com.fengxue.javax_backend.dao.UserAccountRepository;
import com.fengxue.javax_backend.dao.UserLoginRepository;
import com.fengxue.javax_backend.entity.Token;
import com.fengxue.javax_backend.entity.UserAccount;
import com.fengxue.javax_backend.entity.UserAccountUpdate;
import com.fengxue.javax_backend.entity.UserLogin;
import com.fengxue.javax_backend.util.MyAnnotation.PassToken;
import com.fengxue.javax_backend.util.MyAnnotation.UserLoginToken;
import com.fengxue.javax_backend.util.Response.Response;
import com.fengxue.javax_backend.util.Response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserAccountManage {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @UserLoginToken
    @GetMapping("/getUserAccount/{name}")
    public ResponseResult getAccount(@PathVariable(name = "name") String name){

        UserAccount baseUser = userAccountRepository.findByUname(name);

        if(baseUser == null) return Response.createFailResp("no such user");
        else{
            System.out.println(baseUser.getUname());
            UserAccount userAccount = userAccountRepository.getById(baseUser.getUid());
            return Response.createOkResp(userAccount);
        }
    }


    //更新信息
    @UserLoginToken
    @PostMapping("/updateUserAccount")
    public ResponseResult updateAccount(UserAccountUpdate user){
        if(user == null) return Response.createFailResp("null value");
        System.out.println(user.toString());
        String name = user.getUname();
        String oldPw = user.getOldUpw();
        String newPw = user.getNewUpw();
        String email = user.getUemail();
        int id = user.getUid();
        System.out.println("--------");
        UserLogin baseUser = userLoginRepository.findByUid(id);
        UserAccount updatedAccount =  new UserAccount(id,name,newPw,email);
        if(baseUser.getUpw().equals(oldPw)){

            if(baseUser.getUname().equals(name)){
                userAccountRepository.save(updatedAccount);
                userLoginRepository.save(new UserLogin(id,name,newPw));
                return Response.createOkResp(updatedAccount);
            }
            else{
                UserLogin checkName = userLoginRepository.findByUname(name);
                if(checkName!=null){
                    return Response.createFailResp("duplicate username");
                }
                else{
                    userAccountRepository.save(updatedAccount);
                    userLoginRepository.save(new UserLogin(id,name,newPw));
                    return Response.createOkResp(updatedAccount);
                }

            }

        }
        else{
            return Response.createFailResp("wrong password");
        }
    }

}
