package com.exe.buddy_english_be.modules.conversation.service;

import com.exe.buddy_english_be.modules.conversation.dto.ChatRequest;
import com.exe.buddy_english_be.modules.conversation.dto.ChatResponse;

public interface ConversationService {
    ChatResponse chat(Long userId, ChatRequest request);
    ChatResponse getHistory(Long userId);
    void resetHistory(Long userId);
}
