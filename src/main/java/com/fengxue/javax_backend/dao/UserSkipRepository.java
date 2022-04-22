package com.fengxue.javax_backend.dao;

import com.fengxue.javax_backend.entity.UserSkip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSkipRepository extends JpaRepository<UserSkip,Integer> {
    UserSkip findByUid(int id);
}
