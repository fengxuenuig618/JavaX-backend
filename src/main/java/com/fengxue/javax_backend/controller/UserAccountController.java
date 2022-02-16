package com.fengxue.javax_backend.controller;


import com.fengxue.javax_backend.dao.UserAccountRepository;
import com.fengxue.javax_backend.entity.UserAccount;
import com.fengxue.javax_backend.util.Response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserAccountController {
    @Autowired
    private UserAccountRepository userRepository;

    //查询所有
    @GetMapping("/users")
    public ResponseResult<List<UserAccount>> selectUsers()
    {
        List<UserAccount> usersList= userRepository.findAll();

        return Response.createOkResp(usersList);
    }

    //根据id查询
    @GetMapping("/users/{id}")
    public ResponseResult<UserAccount> selectUserById(@PathVariable(name = "id") int id)
    {
        UserAccount user = userRepository.findById(id).get();

        return Response.createOkResp(user);
    }

    //添加
    @PostMapping("/users")
    public ResponseResult addUser(UserAccount user)
    {

        System.out.println(user.toString());
        userRepository.save(user);

        return Response.createOkResp("add success");
    }

    //修改
    @PutMapping("/users")
    public ResponseResult updateUser(UserAccount user)
    {
        userRepository.save(user);

        return Response.createOkResp("edit success");
    }

    //删除
    @DeleteMapping("/users/{id}")
    public ResponseResult<UserAccount> deleteUserById(@PathVariable(name = "id") int id)
    {

        userRepository.deleteById(id);
        return Response.createOkResp("delete success");

    }

    //校验密码
    @PostMapping("/login")
    public ResponseResult loginUser(UserAccount user)
    {
        String name = user.getUname();
        String pwd = user.getUpw();
        System.out.println("=--------");
        System.out.println(name);
        System.out.println("----------");
        List<UserAccount> users = userRepository.findByUname(name);
        for(UserAccount us:users){
            System.out.println(us.getUname());
        }
        if(users.isEmpty()) return Response.createFailResp("no such user");
        if(users.get(0).getUpw().equals(pwd)){
            return Response.createOkResp("login success");
        }
        else{
            return Response.createFailResp("wrong password");
        }

    }
}
