package com.fengxue.javax_backend.dao;

import com.fengxue.javax_backend.entity.UserFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFeedbackRepository extends JpaRepository<UserFeedback,Integer> {
}
