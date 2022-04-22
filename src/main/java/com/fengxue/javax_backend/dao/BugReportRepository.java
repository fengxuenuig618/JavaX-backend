package com.fengxue.javax_backend.dao;

import com.fengxue.javax_backend.entity.BugReport;
import com.fengxue.javax_backend.entity.ChapterTutorial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugReportRepository extends JpaRepository<BugReport,Integer> {
}
