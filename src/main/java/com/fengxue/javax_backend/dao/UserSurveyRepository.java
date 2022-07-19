package com.fengxue.javax_backend.dao;

import com.fengxue.javax_backend.entity.UserQuiz;
import com.fengxue.javax_backend.entity.UserSurvey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSurveyRepository extends JpaRepository<UserSurvey,Integer> {
}
