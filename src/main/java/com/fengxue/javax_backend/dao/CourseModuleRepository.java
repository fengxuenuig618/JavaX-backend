package com.fengxue.javax_backend.dao;

import com.fengxue.javax_backend.entity.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseModuleRepository extends JpaRepository<CourseModule,Integer> {

    CourseModule findByModuleId(String id);
}
