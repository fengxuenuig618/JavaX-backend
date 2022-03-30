package com.fengxue.javax_backend.dao;

import com.fengxue.javax_backend.entity.CourseMcq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseMcqRepository extends JpaRepository<CourseMcq,Integer> {
    CourseMcq findByMcqId(String id);
    List<CourseMcq> findAllByChapterBelong(String id);
    List<CourseMcq> findAllByChapterBelongAndGlobalLevelLessThanEqual(String id,int level);
    int countAllByModuleBelongAndGlobalLevelLessThanEqual(String id, int level);
}
