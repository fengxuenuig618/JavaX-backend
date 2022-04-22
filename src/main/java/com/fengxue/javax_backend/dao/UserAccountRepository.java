package com.fengxue.javax_backend.dao;

import com.fengxue.javax_backend.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface UserAccountRepository extends JpaRepository<UserAccount,Integer> {
    UserAccount findByUname(String userName);

    UserAccount findByUid(int id);

    UserAccount findByUemail(String email);
}
