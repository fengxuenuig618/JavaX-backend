package com.fengxue.javax_backend.dao;

import com.fengxue.javax_backend.entity.CourseChapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseChapterRepository extends JpaRepository<CourseChapter,Integer> {
    List<CourseChapter> findAllByModuleBelong(String moduleId);
}
