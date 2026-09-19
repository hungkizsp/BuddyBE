package com.exe.buddy_english_be.modules.studymode.repository;

import com.exe.buddy_english_be.modules.studymode.entity.StudySession;
import com.exe.buddy_english_be.modules.studymode.enums.StudyMode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    List<StudySession> findByChildIdOrderByCreatedAtDesc(Long childId);

    List<StudySession> findByChildIdAndModeOrderByCreatedAtDesc(Long childId, StudyMode mode);
}
