package com.exe.buddy_english_be.modules.conversation.repository;

import com.exe.buddy_english_be.modules.conversation.entity.ConversationContext;
import com.exe.buddy_english_be.modules.conversation.enums.ContextType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationContextRepository extends JpaRepository<ConversationContext, Long> {

    List<ConversationContext> findBySessionIdAndIsActiveTrue(Long sessionId);

    Optional<ConversationContext> findBySessionIdAndContextKeyAndContextType(Long sessionId, String contextKey, ContextType contextType);
}
