package com.fengxue.javax_backend.controller;

import com.fengxue.javax_backend.dao.UserAccountRepository;
import com.fengxue.javax_backend.dao.UserDateRepository;
import com.fengxue.javax_backend.entity.*;
import com.fengxue.javax_backend.util.DataProcess;
import com.fengxue.javax_backend.util.MyAnnotation.UserLoginToken;
import com.fengxue.javax_backend.util.Response.Response;
import com.fengxue.javax_backend.util.Response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserDateController {

    @Autowired
    private UserDateRepository userDateRepository;

    @UserLoginToken
    @GetMapping("/getUserDate/{id}")
    public ResponseResult getUserDate(@PathVariable(name = "id") int id){
        UserDate baseDate = userDateRepository.findByUid(id);
        UserDateTransfer retDate = new UserDateTransfer();
        retDate.setUid(id);
        if(baseDate!=null){
            for(String curDate: DataProcess.getDelimitArray(baseDate.getDateRecord(),";")){
                retDate.getHistoryDate().add(curDate);
            }
        }
        return Response.createOkResp(retDate);
    }

    @UserLoginToken
    @PostMapping("/postUserNewDate/{id}")
    public ResponseResult postUserDate(@PathVariable(name = "id") int id,UserDateTransfer user){
        System.out.println("====================");
        System.out.println(user.toString());
        System.out.println("====================");
        UserDate baseDate = userDateRepository.findByUid(id);
        if(baseDate==null){
            UserDate newDate = new UserDate();
            newDate.setUid(id);
            newDate.setDateRecord(user.getNewDate()+";");
            userDateRepository.save(newDate);
        }
        else{
            String newDate = baseDate.getDateRecord();
            newDate+=user.getNewDate();
            newDate+=";";
            baseDate.setDateRecord(newDate);
            userDateRepository.save(baseDate);
        }
      return Response.createOkResp();

    }
}
