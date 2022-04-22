package com.fengxue.javax_backend.dao;

import com.fengxue.javax_backend.entity.UserQuiz;
import com.fengxue.javax_backend.entity.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserVerificationRepository extends JpaRepository<UserVerification,Integer> {
    UserVerification findByEmail(String email);
}
