package com.exe.buddy_english_be.modules.conversation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.exe.buddy_english_be.modules.conversation.dto.ChatRequest;
import com.exe.buddy_english_be.modules.conversation.dto.ChatResponse;
import com.exe.buddy_english_be.modules.conversation.entity.ConversationSession;
import com.exe.buddy_english_be.modules.conversation.entity.ConversationMessage;
import com.exe.buddy_english_be.modules.conversation.entity.ConversationContext;
import com.exe.buddy_english_be.modules.conversation.enums.SessionStatus;
import com.exe.buddy_english_be.modules.conversation.enums.MessageSender;
import com.exe.buddy_english_be.modules.conversation.enums.MessageType;
import com.exe.buddy_english_be.modules.conversation.enums.ContextType;
import com.exe.buddy_english_be.modules.conversation.repository.ConversationSessionRepository;
import com.exe.buddy_english_be.modules.conversation.repository.ConversationMessageRepository;
import com.exe.buddy_english_be.modules.conversation.repository.ConversationContextRepository;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import com.exe.buddy_english_be.modules.progress.entity.ChildVocabularyProgress;
import com.exe.buddy_english_be.modules.progress.repository.ChildVocabularyProgressRepository;
import com.exe.buddy_english_be.modules.vocabulary.entity.Vocabulary;
import com.exe.buddy_english_be.modules.vocabulary.repository.VocabularyRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;
import com.exe.buddy_english_be.shared.exception.GeminiQuotaExceededException;
import com.exe.buddy_english_be.shared.exception.GeminiUnavailableException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationSessionRepository sessionRepository;
    private final ConversationMessageRepository messageRepository;
    private final ConversationContextRepository contextRepository;
    private final ChildProfileRepository childProfileRepository;
    private final ChildVocabularyProgressRepository vocabularyProgressRepository;
    private final VocabularyRepository vocabularyRepository;
    private final GeminiService geminiService;

    @Override
    @Transactional
    public ChatResponse chat(Long userId, ChatRequest request) {
        log.info("Processing chat in Conversation Engine for user: {}", userId);
        ChildProfile child = childProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        ConversationSession session = getOrCreateSession(child);

        // Save incoming user message
        ConversationMessage userMsg = ConversationMessage.builder()
                .session(session)
                .sender(MessageSender.CHILD)
                .message(trim(request.message(), 2000))
                .messageType(MessageType.TEXT)
                .createdAt(LocalDateTime.now())
                .build();
        messageRepository.save(userMsg);
        session.setTotalMessages(session.getTotalMessages() + 1);

        List<Vocabulary> learnedVocabulary = getLearnedVocabulary(child.getId());

        if (learnedVocabulary.isEmpty()) {
            String reply = "You have not learned any vocabulary yet. Learn a new topic first, then I can practice it with you.";
            saveBuddyMessage(session, reply);
            return ChatResponse.builder()
                    .reply(reply)
                    .summary("")
                    .allLearnedVocabularyAsked(false)
                    .build();
        }

        // Get current vocabulary under practice from context
        Long prevVocabularyId = null;
        Optional<ConversationContext> currentVocabCtx = contextRepository
                .findBySessionIdAndContextKeyAndContextType(session.getId(), "current_vocab_id", ContextType.LEARNING);
        if (currentVocabCtx.isPresent() && currentVocabCtx.get().getIsActive()) {
            try {
                prevVocabularyId = Long.parseLong(currentVocabCtx.get().getContextValue());
            } catch (NumberFormatException ignored) {}
        }

        Vocabulary prevVocabulary = null;
        if (prevVocabularyId != null) {
            Long finalPrevId = prevVocabularyId;
            prevVocabulary = learnedVocabulary.stream()
                    .filter(v -> v.getId().equals(finalPrevId))
                    .findFirst()
                    .orElse(null);
        }

        // Find asked vocabulary IDs from context records
        List<ConversationContext> activeContexts = contextRepository.findBySessionIdAndIsActiveTrue(session.getId());
        Set<Long> askedIds = activeContexts.stream()
                .filter(ctx -> "asked_vocab_id".equals(ctx.getContextKey()))
                .map(ctx -> {
                    try {
                        return Long.parseLong(ctx.getContextValue());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Optional<Vocabulary> nextVocabulary = learnedVocabulary.stream()
                .filter(vocabulary -> !askedIds.contains(vocabulary.getId()))
                .findFirst();

        boolean allAsked = nextVocabulary.isEmpty();
        Vocabulary vocabulary = null;

        if (!allAsked) {
            vocabulary = nextVocabulary.get();
            askedIds.add(vocabulary.getId());

            // Save asked vocabulary to context
            ConversationContext askedCtx = ConversationContext.builder()
                    .session(session)
                    .contextKey("asked_vocab_id")
                    .contextValue(vocabulary.getId().toString())
                    .contextType(ContextType.LEARNING)
                    .isActive(true)
                    .build();
            contextRepository.save(askedCtx);

            // Update current vocabulary context
            if (currentVocabCtx.isPresent()) {
                ConversationContext currentCtx = currentVocabCtx.get();
                currentCtx.setContextValue(vocabulary.getId().toString());
                currentCtx.setIsActive(true);
                contextRepository.save(currentCtx);
            } else {
                ConversationContext currentCtx = ConversationContext.builder()
                        .session(session)
                        .contextKey("current_vocab_id")
                        .contextValue(vocabulary.getId().toString())
                        .contextType(ContextType.LEARNING)
                        .isActive(true)
                        .build();
                contextRepository.save(currentCtx);
            }
        } else {
            // Clear current vocab practice
            if (currentVocabCtx.isPresent()) {
                ConversationContext currentCtx = currentVocabCtx.get();
                currentCtx.setIsActive(false);
                contextRepository.save(currentCtx);
            }
        }

        // Construct context details
        String vocabList = learnedVocabulary.stream()
                .map(v -> String.format("- %s (Meaning: %s, Category: %s)",
                        v.getWord(),
                        v.getMeaning(),
                        v.getCategory() != null ? v.getCategory().getName() : "General"))
                .collect(Collectors.joining("\n"));

        String systemInstruction = String.format(
                """
                        You are "Buddy", a friendly, patient, and encouraging English-learning assistant.
                        Your task is to help the user practice their learned English vocabulary words in a conversational way.

                        Guidelines:
                        1. Always keep your response concise (under 3-4 sentences) and natural, like a chat messenger.
                        2. Match the user's English level (Level %d). Use vocabulary and grammar appropriate for this level.
                        3. The user's nickname is: %s.
                        4. The user has learned these vocabulary words so far:
                        %s

                        Your responses should follow these rules:
                        - If the user was practicing a word in their previous turn, analyze the user's latest input. If they used the word correctly, praise them. If they made a grammatical error or used it incorrectly, provide friendly, gentle corrections and explain why.
                        - If there is a new vocabulary word to practice, transition naturally and ask the user to use the new word in a sentence or explain a situation using it.
                        - If all vocabulary words have been practiced, congratulate the user, offer to review any of the words, or encourage them to learn new topics.
                        """,
                child.getLevel() != null ? child.getLevel() : 1,
                child.getNickname() != null ? child.getNickname() : "Learner",
                vocabList);

        String sessionSummary = getSummaryFromContext(session.getId());

        StringBuilder userPromptBuilder = new StringBuilder();
        if (sessionSummary != null && !sessionSummary.isBlank()) {
            userPromptBuilder.append("Conversation history summary so far:\n")
                    .append(sessionSummary)
                    .append("\n\n");
        }

        if (prevVocabulary != null) {
            userPromptBuilder.append("Previous word the user was practicing: \"")
                    .append(prevVocabulary.getWord())
                    .append("\" (Meaning: \"")
                    .append(prevVocabulary.getMeaning())
                    .append("\")\n");
        }

        if (vocabulary != null) {
            userPromptBuilder.append("Next word to practice: \"")
                    .append(vocabulary.getWord())
                    .append("\" (Meaning: \"")
                    .append(vocabulary.getMeaning())
                    .append("\", Category: \"")
                    .append(vocabulary.getCategory() != null ? vocabulary.getCategory().getName() : "General")
                    .append("\")\n");
        } else {
            userPromptBuilder.append("No new words left to practice. All words have been asked.\n");
        }

        userPromptBuilder.append("\nUser's message: ").append(request.message());
        String userPrompt = userPromptBuilder.toString();

        // 1. Build hardcoded fallback reply
        String fallbackReply;
        if (vocabulary != null) {
            fallbackReply = buildPracticeReply(session.getTotalMessages(), vocabulary);
        } else {
            fallbackReply = "Nice work. We have already practiced every vocabulary word you have learned so far. Learn a new topic and I will make fresh questions for you.";
        }

        // 2. Call Gemini; fall back to hardcoded reply if AI is unavailable
        String reply;
        try {
            reply = geminiService.generateResponse(systemInstruction, userPrompt);
        } catch (GeminiQuotaExceededException e) {
            log.warn("Gemini quota exceeded for userId: {}. Activating hardcoded fallback.", userId, e);
            reply = fallbackReply;
        } catch (GeminiUnavailableException e) {
            log.warn("Gemini unavailable for userId: {}. Activating hardcoded fallback.", userId, e);
            reply = fallbackReply;
        }

        // Save AI message response
        saveBuddyMessage(session, reply);

        // Update summary context
        updateSummaryContext(session, sessionSummary, request.message(), reply, allAsked);

        sessionRepository.save(session);

        return ChatResponse.builder()
                .reply(reply)
                .summary(getSummaryFromContext(session.getId()))
                .vocabularyWord(vocabulary == null ? null : vocabulary.getWord())
                .vocabularyMeaning(vocabulary == null ? null : vocabulary.getMeaning())
                .vocabularyCategory(vocabulary == null ? null : (vocabulary.getCategory() != null ? vocabulary.getCategory().getName() : "General"))
                .allLearnedVocabularyAsked(allAsked)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ChatResponse getHistory(Long userId) {
        ChildProfile child = childProfileRepository.findByUserId(userId)
                .orElse(null);
        if (child == null) {
            return ChatResponse.builder().reply("").summary("").allLearnedVocabularyAsked(false).build();
        }

        ConversationSession session = sessionRepository
                .findTopByChildIdAndStatusOrderByStartedAtDesc(child.getId(), SessionStatus.ACTIVE)
                .orElse(null);

        if (session == null) {
            return ChatResponse.builder().reply("").summary("").allLearnedVocabularyAsked(false).build();
        }

        List<ConversationMessage> messages = messageRepository.findTop20BySessionIdOrderByCreatedAtDesc(session.getId());
        String lastReply = "";
        for (ConversationMessage msg : messages) {
            if (msg.getSender() == MessageSender.BUDDY) {
                lastReply = msg.getMessage();
                break;
            }
        }

        return ChatResponse.builder()
                .reply(lastReply)
                .summary(getSummaryFromContext(session.getId()))
                .allLearnedVocabularyAsked(false)
                .build();
    }

    @Override
    @Transactional
    public void resetHistory(Long userId) {
        ChildProfile child = childProfileRepository.findByUserId(userId).orElse(null);
        if (child != null) {
            // Close active session
            Optional<ConversationSession> sessionOpt = sessionRepository
                    .findTopByChildIdAndStatusOrderByStartedAtDesc(child.getId(), SessionStatus.ACTIVE);
            if (sessionOpt.isPresent()) {
                ConversationSession session = sessionOpt.get();
                session.setStatus(SessionStatus.COMPLETED);
                session.setEndedAt(LocalDateTime.now());
                sessionRepository.save(session);
            }
        }
    }

    private ConversationSession getOrCreateSession(ChildProfile child) {
        return sessionRepository.findTopByChildIdAndStatusOrderByStartedAtDesc(child.getId(), SessionStatus.ACTIVE)
                .orElseGet(() -> sessionRepository.save(ConversationSession.builder()
                        .child(child)
                        .startedAt(LocalDateTime.now())
                        .status(SessionStatus.ACTIVE)
                        .totalMessages(0)
                        .build()));
    }

    private List<Vocabulary> getLearnedVocabulary(Long childId) {
        return vocabularyProgressRepository.findByChildIdOrderByLastPracticedDesc(childId).stream()
                .map(ChildVocabularyProgress::getVocabulary)
                .filter(Objects::nonNull)
                .filter(vocab -> vocab.getWord() != null && !vocab.getWord().isBlank())
                .collect(Collectors.toList());
    }

    private void saveBuddyMessage(ConversationSession session, String reply) {
        ConversationMessage buddyMsg = ConversationMessage.builder()
                .session(session)
                .sender(MessageSender.BUDDY)
                .message(reply)
                .messageType(MessageType.TEXT)
                .createdAt(LocalDateTime.now())
                .build();
        messageRepository.save(buddyMsg);
        session.setTotalMessages(session.getTotalMessages() + 1);
    }

    private String getSummaryFromContext(Long sessionId) {
        return contextRepository
                .findBySessionIdAndContextKeyAndContextType(sessionId, "session_summary", ContextType.SHORT_TERM)
                .map(ConversationContext::getContextValue)
                .orElse("");
    }

    private void updateSummaryContext(
            ConversationSession session,
            String existingSummary,
            String userMsg,
            String buddyReply,
            boolean allAsked) {

        String status = allAsked ? " [ALL_DONE]" : "";
        String compactUser = truncate(normalize(userMsg), 80);
        String compactBuddy = truncate(normalize(buddyReply), 120);
        String nextLine = "U: " + compactUser + " | B: " + compactBuddy + status;

        String newSummary;
        if (existingSummary == null || existingSummary.isBlank()) {
            newSummary = nextLine;
        } else {
            newSummary = existingSummary + "\n" + nextLine;
        }

        // Limit turns to 5
        String[] lines = newSummary.split("\n");
        if (lines.length > 5) {
            StringBuilder sb = new StringBuilder();
            for (int i = lines.length - 5; i < lines.length; i++) {
                if (sb.length() > 0) sb.append("\n");
                sb.append(lines[i]);
            }
            newSummary = sb.toString();
        }

        Optional<ConversationContext> summaryCtxOpt = contextRepository
                .findBySessionIdAndContextKeyAndContextType(session.getId(), "session_summary", ContextType.SHORT_TERM);

        if (summaryCtxOpt.isPresent()) {
            ConversationContext summaryCtx = summaryCtxOpt.get();
            summaryCtx.setContextValue(newSummary);
            contextRepository.save(summaryCtx);
        } else {
            ConversationContext summaryCtx = ConversationContext.builder()
                    .session(session)
                    .contextKey("session_summary")
                    .contextValue(newSummary)
                    .contextType(ContextType.SHORT_TERM)
                    .isActive(true)
                    .build();
            contextRepository.save(summaryCtx);
        }
    }

    private String buildPracticeReply(int totalMessages, Vocabulary vocabulary) {
        int turn = totalMessages / 2 + 1;
        String word = vocabulary.getWord();
        String meaning = vocabulary.getMeaning() == null || vocabulary.getMeaning().isBlank()
                ? "its meaning"
                : vocabulary.getMeaning();
        String category = vocabulary.getCategory() != null
                ? vocabulary.getCategory().getName()
                : "your vocabulary list";

        return switch (turn % 3) {
            case 1 -> "Let's practice \"" + word + "\" from " + category
                    + ". It means \"" + meaning + "\". Can you write one short sentence using \"" + word + "\"?";
            case 2 -> "Good, let's try a new angle. What is a real-life situation where you could use \""
                    + word + "\"? Keep it simple and natural.";
            default -> "Quick check: choose the better meaning for \"" + word
                    + "\": \"" + meaning + "\" or something different? After that, make your own example.";
        };
    }

    private String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\s+", " ").trim();
    }

    private String trim(String value, int limit) {
        String normalized = normalize(value);
        return normalized.length() <= limit ? normalized : normalized.substring(0, limit);
    }

    private String truncate(String value, int maxLen) {
        if (value == null) return "";
        return value.length() <= maxLen ? value : value.substring(0, maxLen) + "...";
    }
}
