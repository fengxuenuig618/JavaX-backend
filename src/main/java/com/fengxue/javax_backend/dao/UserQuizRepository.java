package com.fengxue.javax_backend.dao;

import com.fengxue.javax_backend.entity.UserQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuizRepository extends JpaRepository<UserQuiz,Integer> {
    UserQuiz findByUid(int id);
}
