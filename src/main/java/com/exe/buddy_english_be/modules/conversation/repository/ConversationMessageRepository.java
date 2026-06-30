package com.exe.buddy_english_be.modules.conversation.repository;

import com.exe.buddy_english_be.modules.conversation.entity.ConversationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationMessageRepository extends JpaRepository<ConversationMessage, Long> {

    List<ConversationMessage> findBySessionIdOrderByCreatedAtAsc(Long sessionId);

    List<ConversationMessage> findTop20BySessionIdOrderByCreatedAtDesc(Long sessionId);
}
