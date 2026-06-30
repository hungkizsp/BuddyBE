package com.exe.buddy_english_be.modules.conversation.repository;

import com.exe.buddy_english_be.modules.conversation.entity.AiAnalysisLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiAnalysisLogRepository extends JpaRepository<AiAnalysisLog, Long> {
}
