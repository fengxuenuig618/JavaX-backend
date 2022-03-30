package com.fengxue.javax_backend.dao;
import com.fengxue.javax_backend.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface UserLoginRepository extends JpaRepository<UserLogin,Integer>{
    UserLogin findByUname(String userName);
    UserLogin findByUid(int userID);
    List<UserLogin> findAllByUname(String userName);
}
