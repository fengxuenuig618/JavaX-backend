package com.fengxue.javax_backend.controller;


import com.fengxue.javax_backend.dao.UserAccountRepository;
import com.fengxue.javax_backend.entity.UserAccount;
import com.fengxue.javax_backend.entity.UserLoginTransfer;
import com.fengxue.javax_backend.util.MyAnnotation.UserLoginToken;
import com.fengxue.javax_backend.util.Response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测试用
 * */
@RestController
public class UserAccountController {
    @Autowired
    private UserAccountRepository userAccountRepository;



    //查询所有
    @UserLoginToken
    @GetMapping("/usersl")
    public ResponseResult<List<UserAccount>> selectUsersl()
    {
        List<UserAccount> usersList= userAccountRepository.findAll();

        return Response.createOkResp(usersList);
    }

    //查询所有
    @GetMapping("/users")
    public ResponseResult<List<UserAccount>> selectUsers()
    {
        List<UserAccount> usersList= userAccountRepository.findAll();

        return Response.createOkResp(usersList);
    }

    //根据id查询
    @GetMapping("/users/{id}")
    public ResponseResult<UserAccount> selectUserById(@PathVariable(name = "id") int id)
    {
        UserAccount user = userAccountRepository.findById(id).get();

        return Response.createOkResp(user);
    }

    //添加
    @PostMapping("/users")
    public ResponseResult addUser(UserAccount user)
    {

        System.out.println(user.toString());
        userAccountRepository.save(user);

        return Response.createOkResp("add success");
    }

    //修改
    @PutMapping("/users")
    public ResponseResult updateUser(UserAccount user)
    {
        userAccountRepository.save(user);

        return Response.createOkResp("edit success");
    }

    //删除
    @DeleteMapping("/users/{id}")
    public ResponseResult<UserAccount> deleteUserById(@PathVariable(name = "id") int id)
    {

        userAccountRepository.deleteById(id);
        return Response.createOkResp("delete success");

    }


}
