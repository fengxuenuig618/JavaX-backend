package com.fengxue.javax_backend.controller;


import com.fengxue.javax_backend.dao.UserAccountRepository;
import com.fengxue.javax_backend.dao.UserVerificationRepository;
import com.fengxue.javax_backend.entity.*;
import com.fengxue.javax_backend.util.Email.EmailUtil;
import com.fengxue.javax_backend.util.MyAnnotation.PassToken;
import com.fengxue.javax_backend.util.MyAnnotation.UserLoginToken;
import com.fengxue.javax_backend.util.Response.Response;
import com.fengxue.javax_backend.util.Response.ResponseResult;
import com.fengxue.javax_backend.util.Standardize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class UserAccountManage<Static> {

    //30分钟有效
    final long VERIFICATION_CODE_TTL = 1800000;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private EmailUtil mailService;

    @Autowired
    private UserVerificationRepository userVerificationRepository;


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



    //发送验证码
    @PassToken
    @GetMapping("/checkUserAccount/{account}")
    public ResponseResult checkAccount(@PathVariable(name = "account") String account){

        boolean valid = false;
        int id = 0;
        String email = "";
        if (userAccountRepository.findByUemail(account) != null){
            valid = true;
            id = userAccountRepository.findByUemail(account).getUid();
            email = userAccountRepository.findByUemail(account).getUemail();
        }
        if (userAccountRepository.findByUname(account) != null){
            valid = true;
            id = userAccountRepository.findByUname(account).getUid();
            email = userAccountRepository.findByUname(account).getUemail();
        }

        if(valid){
            UserVerification userVerification = UserAccountManage.getVerification(email);
            userVerificationRepository.save(userVerification);
            String mailBody = "<h2> Your verification code is</h2>\n" +
                    "<h1 style=\"font-size: 100px\">"+userVerification.getVerificationCode()+"</h1>\n" +
                    "<h3>The verification code is valid in 30 minutes</h3>\n" +
                    "<h4>JavaX Official (Contact the author:fengxue618@gmail.com)</h4>";

            //mailService.sendSimpleMail("fengxue618@gmail.com","主题：你好普通邮件","内容：第一封邮件");
            mailService.sendHtmlMail(email,"JavaX: Verification Code", mailBody);
            return Response.createOkResp(email);

        }
        else{
            return Response.createFailResp("no such user");
        }

    }

    //重置密码
    @PassToken
    @PostMapping("/resetPassword")
    public ResponseResult resetPassword(ResetPasswordTransfer resetPasswordTransfer){
        System.out.println(resetPasswordTransfer.toString());
        String email = resetPasswordTransfer.getEmail();
        UserAccount baseUser = userAccountRepository.findByUemail(email);
        if(baseUser == null) return Response.createFailResp();
        else{
            baseUser.setUpw(resetPasswordTransfer.getPassword());
            userAccountRepository.save(baseUser);
            return Response.createOkResp();
        }
    }


    //校验验证码
    @PassToken
    @PostMapping("/checkVerificationCode")
    public ResponseResult checkVerificationCode(UserVerification userVerification){
        if(userVerification.getEmail().equals("")) return Response.createFailResp("time out");
        UserVerification dataVerification = userVerificationRepository.findByEmail(userVerification.getEmail());
        if(dataVerification.getVerificationCode().equals(userVerification.getVerificationCode())){
            if(dataVerification.getTimeStamp() + VERIFICATION_CODE_TTL < userVerification.getTimeStamp()){
                return Response.createFailResp("time out");
            }
            else {
                return Response.createOkResp();
            }
        }
        else{
            return Response.createFailResp("wrong code");
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
        String email = Standardize.getEmail(user.getUemail());
        int id = user.getUid();
        System.out.println("--------");
        System.out.println(email);
        System.out.println("--------");
        UserAccount baseUser = userAccountRepository.findByUid(id);
        UserAccount updatedAccount =  new UserAccount(id,name,newPw,email);
        if(baseUser.getUpw().equals(oldPw)){


            if(baseUser.getUname().equals(name)){

                if(userAccountRepository.findByUemail(email) != null){

                    return Response.createFailResp("duplicate email");
                }
                userAccountRepository.save(updatedAccount);
                return Response.createOkResp(updatedAccount);
            }
            else{
                UserAccount checkName = userAccountRepository.findByUname(name);

                if(checkName!=null){

                    return Response.createFailResp("duplicate username");
                }
                if(userAccountRepository.findByUemail(email) != null){

                    return Response.createFailResp("duplicate email");
                }

                    userAccountRepository.save(updatedAccount);
                    return Response.createOkResp(updatedAccount);


            }

        }
        else{
            return Response.createFailResp("wrong password");
        }
    }

    public static UserVerification getVerification (String email){
        UserVerification userVerification = new UserVerification();
        userVerification.setEmail(email);
        String code = "";
        for(int i=0;i<4;i++){
            code += String.valueOf ((int)(1+Math.random()*(9-1+1)));
        }
        userVerification.setVerificationCode(code);
        long stamp = new Date().getTime();
        userVerification.setTimeStamp(stamp);
        return userVerification;
    }


}
