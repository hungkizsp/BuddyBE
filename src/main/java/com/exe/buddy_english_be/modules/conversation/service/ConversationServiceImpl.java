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
            String reply = "Bé chưa học từ vựng nào nè. Bé hãy học xong bài mới rồi quay lại luyện tập cùng Bolly nhé!";
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
            } catch (NumberFormatException ignored) {
            }
        }

        Vocabulary prevVocabulary = null;
        if (prevVocabularyId != null) {
            Long finalPrevId = prevVocabularyId;
            prevVocabulary = learnedVocabulary.stream()
                    .filter(v -> v.getId().equals(finalPrevId))
                    .findFirst()
                    .orElse(null);
        }

        // Find asked vocabulary IDs from context (single row, comma-separated)
        Optional<ConversationContext> askedCtxOpt = contextRepository
                .findBySessionIdAndContextKeyAndContextType(session.getId(), "asked_vocab_id", ContextType.LEARNING);

        Set<Long> askedIds = new HashSet<>();
        if (askedCtxOpt.isPresent() && askedCtxOpt.get().getIsActive()) {
            String raw = askedCtxOpt.get().getContextValue();
            if (raw != null && !raw.isBlank()) {
                for (String part : raw.split(",")) {
                    try {
                        askedIds.add(Long.parseLong(part.trim()));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }

        Optional<Vocabulary> nextVocabulary = learnedVocabulary.stream()
                .filter(vocabulary -> !askedIds.contains(vocabulary.getId()))
                .findFirst();

        boolean allAsked = nextVocabulary.isEmpty();
        Vocabulary vocabulary = null;

        if (!allAsked) {
            vocabulary = nextVocabulary.get();
            askedIds.add(vocabulary.getId());

            // Save/update asked vocabulary to context (single row, comma-separated)
            String newAskedValue = askedIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            if (askedCtxOpt.isPresent()) {
                ConversationContext askedCtx = askedCtxOpt.get();
                askedCtx.setContextValue(newAskedValue);
                askedCtx.setIsActive(true);
                contextRepository.save(askedCtx);
            } else {
                ConversationContext askedCtx = ConversationContext.builder()
                        .session(session)
                        .contextKey("asked_vocab_id")
                        .contextValue(newAskedValue)
                        .contextType(ContextType.LEARNING)
                        .isActive(true)
                        .build();
                contextRepository.save(askedCtx);
            }

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
                        v.getCategory() != null ? v.getCategory().getName() : "Chung"))
                .collect(Collectors.joining("\n"));

        String systemInstruction = String.format(
                """
                        You are "Bolly", a friendly, patient, and cheerful English-learning assistant for children aged 6 to 10 years old.
                        Your goal is to practice English vocabulary with the child in a fun and encouraging way.

                        CRITICAL RULES:
                        1. Always reply in VIETNAMESE, but keep the target English vocabulary words in English so the child can practice.
                        2. Keep responses VERY SHORT and SIMPLE (1 to 2 short sentences). Use language suitable for a 6-10 year old child (use words like "bé", "nhé", "Bolly").
                        3. STRICTLY DO NOT use Markdown formatting anywhere (NO asterisks **, NO hashtags #, NO bullet points, NO underline). Plain text only!
                        4. Child's nickname: %s.

                        Conversation Guide:
                        - If the child was practicing a word previously, check their answer gently. Praise them if correct, or kindly encourage them if wrong.
                        - If there is a new word to practice, introduce it simply in Vietnamese and ask the child to repeat, translate, or make a short sentence with it.
                        - If all words are completed, praise the child warmly and encourage them to learn new topics.
                        """,
                child.getNickname() != null ? child.getNickname() : "bé",
                vocabList);

        String sessionSummary = getSummaryFromContext(session.getId());

        StringBuilder userPromptBuilder = new StringBuilder();
        if (sessionSummary != null && !sessionSummary.isBlank()) {
            userPromptBuilder.append("Conversation history summary:\n")
                    .append(sessionSummary)
                    .append("\n\n");
        }

        if (prevVocabulary != null) {
            userPromptBuilder.append("Previous practicing word: \"")
                    .append(prevVocabulary.getWord())
                    .append("\" (Meaning: \"")
                    .append(prevVocabulary.getMeaning())
                    .append("\")\n");
        }

        if (vocabulary != null) {
            userPromptBuilder.append("New word to practice now: \"")
                    .append(vocabulary.getWord())
                    .append("\" (Meaning: \"")
                    .append(vocabulary.getMeaning())
                    .append("\", Category: \"")
                    .append(vocabulary.getCategory() != null ? vocabulary.getCategory().getName() : "General")
                    .append("\")\n");
        } else {
            userPromptBuilder.append("All vocabulary words have been practiced.\n");
        }

        userPromptBuilder.append("\nChild's message: ").append(request.message());
        String userPrompt = userPromptBuilder.toString();

        // 1. Build hardcoded fallback reply in Vietnamese
        String fallbackReply;
        if (vocabulary != null) {
            fallbackReply = buildPracticeReply(session.getTotalMessages(), vocabulary);
        } else {
            fallbackReply = "Giỏi lắm! Chúng mình đã ôn hết từ vựng rồi. Bé hãy học thêm bài mới để tiếp tục chơi cùng Bolly nhé!";
        }

        // 2. Call Gemini; fall back to hardcoded reply if AI is unavailable
        String reply;
        try {
            reply = geminiService.generateResponse(systemInstruction, userPrompt);
            reply = stripMarkdown(reply); // Clean markdown formatting
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
                .vocabularyCategory(vocabulary == null ? null
                        : (vocabulary.getCategory() != null ? vocabulary.getCategory().getName() : "General"))
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

        List<ConversationMessage> messages = messageRepository
                .findTop20BySessionIdOrderByCreatedAtDesc(session.getId());
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
                if (sb.length() > 0)
                    sb.append("\n");
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
        String meaning = (vocabulary.getMeaning() == null || vocabulary.getMeaning().isBlank())
                ? "nghĩa của từ"
                : vocabulary.getMeaning();

        return switch (turn % 3) {
            case 1 -> "Hôm nay chúng mình cùng luyện từ \"" + word + "\" có nghĩa là \"" + meaning
                    + "\" nhé. Bé hãy thử đặt câu đơn giản với từ \"" + word + "\" cho Bolly nghe nào!";
            case 2 -> "Thế bé có biết trong thực tế, khi nào chúng mình dùng từ \"" + word
                    + "\" không? Nhắn cho Bolly biết nhé!";
            default -> "Đố bé biết từ \"" + word + "\" nghĩa là gì nào? Có phải là \"" + meaning + "\" không nhỉ?";
        };
    }

    private String stripMarkdown(String input) {
        if (input == null)
            return "";
        return input.replaceAll("\\*\\*", "")
                .replaceAll("\\*", "")
                .replaceAll("#+", "")
                .replaceAll("`", "")
                .trim();
    }

    private String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\s+", " ").trim();
    }

    private String trim(String value, int limit) {
        String normalized = normalize(value);
        return normalized.length() <= limit ? normalized : normalized.substring(0, limit);
    }

    private String truncate(String value, int maxLen) {
        if (value == null)
            return "";
        return value.length() <= maxLen ? value : value.substring(0, maxLen) + "...";
    }
}