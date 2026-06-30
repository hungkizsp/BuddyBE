package com.exe.buddy_english_be.modules.conversation.repository;

import com.exe.buddy_english_be.modules.conversation.entity.ConversationSession;
import com.exe.buddy_english_be.modules.conversation.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationSessionRepository extends JpaRepository<ConversationSession, Long> {

    Optional<ConversationSession> findTopByChildIdAndStatusOrderByStartedAtDesc(Long childId, SessionStatus status);

    List<ConversationSession> findByChildIdOrderByStartedAtDesc(Long childId);
}
