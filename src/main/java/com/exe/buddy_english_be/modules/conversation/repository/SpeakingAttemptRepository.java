package com.exe.buddy_english_be.modules.conversation.repository;

import com.exe.buddy_english_be.modules.conversation.entity.SpeakingAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeakingAttemptRepository extends JpaRepository<SpeakingAttempt, Long> {

    List<SpeakingAttempt> findByChildIdOrderByCreatedAtDesc(Long childId);

    List<SpeakingAttempt> findBySessionId(Long sessionId);
}
