package com.fengxue.javax_backend.dao;

import com.fengxue.javax_backend.entity.UserAccount;
import com.fengxue.javax_backend.entity.UserDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDateRepository extends JpaRepository<UserDate,Integer> {
    UserDate findByUid(int id);
}
