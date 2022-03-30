package com.fengxue.javax_backend.dao;

import com.fengxue.javax_backend.entity.ChapterTutorial;
import com.fengxue.javax_backend.entity.CourseChapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterTutorialRepository extends JpaRepository<ChapterTutorial,Integer> {
    List<ChapterTutorial> findAllByChapterBelong(String chapterId);
    List<ChapterTutorial> findAllByChapterBelongAndGlobalLevelLessThanEqual(String chapterId, int globalLevel);

}
