package com.exe.buddy_english_be.modules.analytics.repository;

import com.exe.buddy_english_be.modules.analytics.entity.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long> {

    List<WeeklyReport> findByChildId(Long childId);

    Optional<WeeklyReport> findTopByChildIdOrderByWeekStartDesc(Long childId);
}
