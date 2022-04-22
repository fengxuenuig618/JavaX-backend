package com.fengxue.javax_backend.controller;
import com.fengxue.javax_backend.dao.UserAccountRepository;
import com.fengxue.javax_backend.dao.UserProfileSettingRepository;
import com.fengxue.javax_backend.dao.UserVerificationRepository;
import com.fengxue.javax_backend.entity.UserAccount;
import com.fengxue.javax_backend.entity.UserLoginTransfer;
import com.fengxue.javax_backend.entity.UserProfileSetting;
import com.fengxue.javax_backend.entity.UserVerification;
import com.fengxue.javax_backend.util.Email.EmailUtil;
import com.fengxue.javax_backend.util.MyAnnotation.PassToken;
import com.fengxue.javax_backend.util.Response.*;

import com.fengxue.javax_backend.util.Standardize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class UserRegisterController {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private EmailUtil mailService;


    @Autowired
    private UserProfileSettingRepository userProfileSettingRepository;

    @Autowired
    private UserVerificationRepository userVerificationRepository;

    //添加
    @PassToken
    @PostMapping("/userRegister")
    public ResponseResult registerUser(UserAccount user)
    {

        String name = user.getUname();
        String email = Standardize.getEmail(user.getUemail());
        UserAccount baseUser = userAccountRepository.findByUname(name);
        if(baseUser != null){
            return Response.createFailResp("duplicate username");
        }
        else if(userAccountRepository.findByUemail(email)!=null){
            return Response.createFailResp("duplicate email");
        }
        else{
            userAccountRepository.save(user);
            userProfileSettingRepository.save(
                    new UserProfileSetting(user.getUid(),0,0,0,0,0,0,"",new HashMap<String,Boolean>()));
            return Response.createOkResp("register success");
        }

    }

    @PassToken
    @GetMapping("/sendCodeRegister/{account}")
    public ResponseResult sendCodeRegister(@PathVariable(name = "account") String account){

        if (userAccountRepository.findByUemail(account) != null){
            return Response.createFailResp("email exist");
        }
        else{
            UserVerification userVerification = UserAccountManage.getVerification(account);
            userVerificationRepository.save(userVerification);
            String mailBody = "<h2> Your verification code is</h2>\n" +
                    "<h1 style=\"font-size: 100px\">"+userVerification.getVerificationCode()+"</h1>\n" +
                    "<h3>The verification code is valid in 30 minutes</h3>\n" +
                    "<h4>JavaX Official (Contact the author:fengxue618@gmail.com)</h4>";

            mailService.sendHtmlMail(account,"JavaX: Verification Code", mailBody);
            return Response.createOkResp(account);
        }


    }


}
