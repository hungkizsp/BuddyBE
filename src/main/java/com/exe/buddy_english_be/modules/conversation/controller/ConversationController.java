package com.exe.buddy_english_be.modules.conversation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exe.buddy_english_be.modules.conversation.dto.ChatRequest;
import com.exe.buddy_english_be.modules.conversation.dto.ChatResponse;
import com.exe.buddy_english_be.modules.conversation.service.ConversationService;
import com.exe.buddy_english_be.shared.response.ApiResponse;

import jakarta.validation.Valid;

@RestController("modulesConversationController")
@RequestMapping("/api/chatbot")
public class ConversationController {
    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<ChatResponse>> chat(
            Authentication authentication,
            @Valid @RequestBody ChatRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        ChatResponse response = conversationService.chat(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Chat response generated successfully", response));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<ChatResponse>> getHistory(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        ChatResponse response = conversationService.getHistory(userId);
        return ResponseEntity.ok(ApiResponse.success("Chat history fetched successfully", response));
    }

    @DeleteMapping("/history")
    public ResponseEntity<ApiResponse<Void>> resetHistory(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        conversationService.resetHistory(userId);
        return ResponseEntity.ok(ApiResponse.success("Chat history reset successfully", null));
    }
}
