package com.exe.buddy_english_be.modules.speech.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.exe.buddy_english_be.modules.conversation.service.GeminiService;
import com.exe.buddy_english_be.modules.learning.entity.ScenarioStep;
import com.exe.buddy_english_be.modules.learning.repository.ScenarioStepRepository;
import com.exe.buddy_english_be.modules.speech.dto.SpeechRecognitionRequest;
import com.exe.buddy_english_be.modules.speech.dto.SpeechRecognitionResponse;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;
import com.exe.buddy_english_be.shared.exception.GeminiQuotaExceededException;
import com.exe.buddy_english_be.shared.exception.GeminiUnavailableException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpeechRecognitionServiceImpl implements SpeechRecognitionService {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final String SYSTEM_PROMPT = """
            You are a precise intent and entity extractor for a child-friendly English learning game.

            Your job is to understand the player's spoken request and classify it into:
            - intent: the main action the player is trying to perform
            - entity: the item or food the player is referring to

            Rules:
            - Understand natural English, including polite phrases, contractions, and simple variations.
            - Do not force the expected entity. Infer the entity from what the player actually said.
            - Return valid JSON only with this schema:
            {
              "intent": "ORDER_FOOD",
              "entity": "CHICKEN"
            }
            - Use uppercase letters with underscores for both values.
            - If the player is ordering food (appetizer/main/dessert), use "ORDER_FOOD".
            - If the player orders a drink (milk, juice, water), use "ORDER_DRINK".
            - Recognise these menu items exactly:
              Appetizers : BREAD, SOUP, SALAD
              Main course: CHICKEN, PIZZA, RICE
              Dessert    : CAKE, APPLE, ICE_CREAM
              Drinks     : MILK, JUICE, WATER
            - Keep the response concise and valid JSON. Do not add markdown or explanation.
            """;

    private static final String INTENT_ORDER_FOOD = "ORDER_FOOD";

    /**
     * Polite/filler words that carry no food-name information and should be
     * stripped before keyword matching.
     */
    private static final Set<String> IGNORED_WORDS = Set.of(
            "can", "could", "may", "please", "i", "me", "my",
            "have", "want", "like", "order", "get", "id", "a", "an", "the",
            "some", "would", "d", "ll", "s", "to", "for");
    private static final Set<String> DRINK_KEYWORDS = Set.of(
            "milk",
            "juice",
            "water",
            "drink");

    /**
     * Maps one or more transcript keywords → canonical entity value
     * (UPPER_SNAKE_CASE) – aligned with menu2 (Family Restaurant, scenario 3).
     *
     * <p>
     * Menu groups:
     * 1. Khai Vị  : BREAD, SOUP, SALAD
     * 2. Món Chính: CHICKEN, PIZZA, RICE
     * 3. Tráng Miệng: CAKE, APPLE, ICE_CREAM
     * 4. Đồ Uống  : MILK, JUICE, WATER
     */
    private static final Map<String, String> KEYWORD_TO_ENTITY = Map.ofEntries(
            // ── Khai Vị (Appetizers) ─────────────────────────────────────
            Map.entry("bread",        "BREAD"),
            Map.entry("soup",         "SOUP"),
            Map.entry("salad",        "SALAD"),

            // ── Món Chính (Main Course) ──────────────────────────────────
            Map.entry("chicken",      "CHICKEN"),
            Map.entry("pizza",        "PIZZA"),
            Map.entry("rice",         "RICE"),

            // ── Tráng Miệng (Dessert) ────────────────────────────────────
            Map.entry("cake",         "CAKE"),
            Map.entry("apple",        "APPLE"),
            Map.entry("ice cream",    "ICE_CREAM"),
            Map.entry("ice_cream",    "ICE_CREAM"),
            Map.entry("icecream",     "ICE_CREAM"),
            Map.entry("ice",          "ICE_CREAM"),   // fallback single word

            // ── Đồ Uống (Drinks) ─────────────────────────────────────────
            Map.entry("milk",         "MILK"),
            Map.entry("juice",        "JUICE"),
            Map.entry("water",        "WATER"));


    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final GeminiService geminiService;
    private final ScenarioStepRepository scenarioStepRepository;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    // @Override
    // public SpeechRecognitionResponse recognize(Long userId,
    // SpeechRecognitionRequest request) {
    // ScenarioStep scenarioStep = resolveScenarioStep(request);

    // if (request.getScenarioId() != null
    // && scenarioStep.getScenario() != null
    // && scenarioStep.getScenario().getId() != null
    // && !scenarioStep.getScenario().getId().equals(request.getScenarioId())) {
    // throw new BusinessException(ErrorCode.SCENARIO_STEP_NOT_FOUND);
    // }

    // String expectedIntent = normalizeValue(scenarioStep.getExpectedIntent());
    // String expectedEntity = normalizeValue(scenarioStep.getExpectedEntity());

    // // --- Primary path: Gemini ---
    // try {
    // String userPrompt = buildUserPrompt(request, expectedIntent, expectedEntity);
    // log.info("Sending speech recognition request to Gemini for userId: {},
    // stepOrder: {}",
    // userId, request.getStepOrder());

    // String rawResponse = geminiService.generateResponse(SYSTEM_PROMPT,
    // userPrompt);
    // ParsedRecognition parsed = parseGeminiResponse(rawResponse);

    // String detectedIntent = normalizeValue(parsed.intent());
    // String detectedEntity = normalizeValue(parsed.entity());

    // log.info("Gemini recognition result — intent: {}, entity: {}",
    // detectedIntent, detectedEntity);
    // return buildRecognitionResult(request, expectedIntent, expectedEntity,
    // detectedIntent, detectedEntity);

    // } catch (GeminiQuotaExceededException e) {
    // log.warn(
    // "Gemini quota exceeded during speech recognition for userId: {}. Switching to
    // rule-based fallback.",
    // userId, e);
    // } catch (GeminiUnavailableException e) {
    // log.warn("Gemini unavailable during speech recognition for userId: {}.
    // Switching to rule-based fallback.",
    // userId, e);
    // }

    // // --- Fallback path: rule-based manual recognition ---
    // return manualRecognize(request, expectedIntent, expectedEntity, userId);
    // }
    @Override
    public SpeechRecognitionResponse recognize(
            Long userId,
            SpeechRecognitionRequest request) {

        ScenarioStep scenarioStep = resolveScenarioStep(request);

        if (request.getScenarioId() != null
                && scenarioStep.getScenario() != null
                && scenarioStep.getScenario().getId() != null
                && !scenarioStep.getScenario().getId().equals(request.getScenarioId())) {

            throw new BusinessException(ErrorCode.SCENARIO_STEP_NOT_FOUND);
        }

        String expectedIntent = normalizeValue(
                scenarioStep.getExpectedIntent());

        String expectedEntity = normalizeValue(
                scenarioStep.getExpectedEntity());

        // Rule-based manual recognition only
        SpeechRecognitionResponse manualResult = manualRecognize(
                request,
                expectedIntent,
                expectedEntity,
                userId);

        log.info(
                "Manual recognition result for userId: {}, entity: {}",
                userId,
                manualResult.getDetectedEntity());

        return manualResult;
    }
    // -------------------------------------------------------------------------
    // Fallback: rule-based recognition (no AI)
    // -------------------------------------------------------------------------

    /**
     * Detects intent and entity from the raw transcript using keyword matching.
     * Intent is always {@code ORDER_FOOD}; entity is looked up via
     * {@link #KEYWORD_TO_ENTITY}.
     */
    private SpeechRecognitionResponse manualRecognize(
            SpeechRecognitionRequest request,
            String expectedIntent,
            String expectedEntity,
            Long userId) {

        String transcript = request.getTranscript();
        log.info("Rule-based fallback recognition for userId: {}, transcript: \"{}\"", userId, transcript);

        // Step 1 – normalise: lowercase + remove punctuation + collapse whitespace
        String normalised = transcript == null ? ""
                : transcript
                        .toLowerCase(Locale.ROOT)
                        .replaceAll("[^a-z0-9 ]", " ")
                        .replaceAll("\\s+", " ")
                        .trim();
        String detectedIntent = INTENT_ORDER_FOOD;

        for (String keyword : DRINK_KEYWORDS) {
            if (normalised.contains(keyword)) {
                detectedIntent = "ORDER_DRINK";
                break;
            }
        }
        // Step 2 – detect food entity
        String detectedEntity = null;

        // ------------------------------------------------------------
        // 2.1 Match multi-word phrases first
        // Example:
        // "apple juice" -> APPLE_JUICE
        // "grilled chicken" -> GRILLED_CHICKEN
        // "beef steak" -> BEEF_STEAK
        // ------------------------------------------------------------
        List<Map.Entry<String, String>> keywords = KEYWORD_TO_ENTITY.entrySet()
                .stream()
                .filter(entry -> entry.getKey().contains(" "))
                .sorted((a, b) -> Integer.compare(
                        b.getKey().length(),
                        a.getKey().length()))
                .toList();

        for (Map.Entry<String, String> entry : keywords) {
            if (containsWholePhrase(normalised, entry.getKey())) {
                detectedEntity = entry.getValue();
                break;
            }
        }

        // ------------------------------------------------------------
        // 2.2 If no phrase matched, match single words
        // ------------------------------------------------------------
        if (detectedEntity == null) {

            String[] tokens = normalised.split("\\s+");

            for (String token : tokens) {

                if (IGNORED_WORDS.contains(token)) {
                    continue;
                }

                String candidate = KEYWORD_TO_ENTITY.get(token);

                if (candidate != null) {
                    detectedEntity = candidate;
                    break;
                }
            }
        }

        if (detectedEntity == null) {
            log.info("Rule-based fallback could not detect any food entity from transcript: \"{}\"", transcript);
            return SpeechRecognitionResponse.builder()
                    .matched(false)
                    .transcript(transcript)
                    .expectedIntent(expectedIntent)
                    .detectedIntent(detectedIntent)
                    .expectedEntity(expectedEntity)
                    .detectedEntity("")
                    .feedback("Bạn nói gì đó nhưng mình chưa nghe rõ. Hãy thử lại nhé!")
                    .build();
        }

        log.info("Rule-based fallback detected entity: {}", detectedEntity);
        return buildRecognitionResult(request, expectedIntent, expectedEntity, detectedIntent, detectedEntity);
    }

    private boolean containsWholePhrase(String text, String phrase) {

        String pattern = "(^|\\s)"
                + java.util.regex.Pattern.quote(phrase)
                + "(\\s|$)";

        return java.util.regex.Pattern
                .compile(pattern)
                .matcher(text)
                .find();
    }

    // -------------------------------------------------------------------------
    // Shared comparison + response builder
    // -------------------------------------------------------------------------

    /**
     * Compares detected intent/entity against expected values and builds the
     * {@link SpeechRecognitionResponse}. Used by both the Gemini path and the
     * rule-based fallback to avoid duplicating comparison logic.
     */
    private SpeechRecognitionResponse buildRecognitionResult(
            SpeechRecognitionRequest request,
            String expectedIntent,
            String expectedEntity,
            String detectedIntent,
            String detectedEntity) {

        boolean matched = expectedIntent.equals(detectedIntent)
                && expectedEntity.equals(detectedEntity);

        return SpeechRecognitionResponse.builder()
                .matched(matched)
                .transcript(request.getTranscript())
                .expectedIntent(expectedIntent)
                .detectedIntent(detectedIntent)
                .expectedEntity(expectedEntity)
                .detectedEntity(detectedEntity)
                .feedback(buildFeedback(expectedIntent, expectedEntity, detectedIntent, detectedEntity, matched))
                .build();
    }

    // -------------------------------------------------------------------------
    // Gemini response parsing
    // -------------------------------------------------------------------------

    private ParsedRecognition parseGeminiResponse(String rawResponse) {
        if (rawResponse == null || rawResponse.isBlank()) {
            return new ParsedRecognition("", "");
        }

        String cleanedJson = cleanJson(rawResponse);
        if (cleanedJson.isBlank()) {
            return new ParsedRecognition("", "");
        }

        try {
            GeminiRecognitionResult result = objectMapper.readValue(cleanedJson, GeminiRecognitionResult.class);
            return new ParsedRecognition(result.intent(), result.entity());
        } catch (Exception e) {
            log.warn("Unable to parse Gemini response for speech recognition, using empty values: {}", rawResponse, e);
            return new ParsedRecognition("", "");
        }
    }

    private String cleanJson(String rawResponse) {
        if (rawResponse == null) {
            return "";
        }

        String cleaned = rawResponse.trim();
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceAll("^```(?:json)?\\s*", "");
            cleaned = cleaned.replaceAll("\\s*```$", "");
        }
        return cleaned.trim();
    }

    // -------------------------------------------------------------------------
    // Utility helpers
    // -------------------------------------------------------------------------

    private ScenarioStep resolveScenarioStep(SpeechRecognitionRequest request) {
        if (request.getStepId() != null) {
            return scenarioStepRepository.findById(request.getStepId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.SCENARIO_STEP_NOT_FOUND));
        }

        if (request.getScenarioId() != null && request.getStepOrder() != null) {
            return scenarioStepRepository.findByScenarioIdAndStepOrder(request.getScenarioId(), request.getStepOrder())
                    .orElseThrow(() -> new BusinessException(ErrorCode.SCENARIO_STEP_NOT_FOUND));
        }

        throw new BusinessException(ErrorCode.SCENARIO_STEP_NOT_FOUND);
    }

    private String buildUserPrompt(SpeechRecognitionRequest request, String expectedIntent, String expectedEntity) {
        return String.format(
                Locale.ROOT,
                """
                        Expected intent:
                        %s

                        Expected entity:
                        %s

                        User transcript:
                        %s
                        """,
                expectedIntent,
                expectedEntity,
                request.getTranscript());
    }

    private String normalizeValue(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        return value.trim()
                .toUpperCase(Locale.ROOT)
                .replace(' ', '_')
                .replace('-', '_');
    }

    private String buildFeedback(
            String expectedIntent,
            String expectedEntity,
            String detectedIntent,
            String detectedEntity,
            boolean matched) {
        if (matched) {
            return "Chính xác! Buddy muốn gọi " + formatEntity(expectedEntity) + " đó!";
        }

        if (!expectedIntent.equals(detectedIntent)) {
            if ("ORDER_DRINK".equals(expectedIntent)) {
                return "Buddy muốn gọi đồ uống, hãy thử lại nhé!";
            }
            return "Buddy muốn gọi món ăn, hãy thử lại nhé!";
        }

        return "Bạn nói " + formatEntity(detectedEntity) + ", nhưng Buddy muốn " + formatEntity(expectedEntity) + ". Thử lại nhé!";
    }

    private String formatEntity(String entity) {
        if (entity == null || entity.isBlank()) {
            return "the requested item";
        }

        StringBuilder formatted = new StringBuilder();
        for (String part : entity.split("_")) {
            if (part.isBlank()) {
                continue;
            }
            if (formatted.length() > 0) {
                formatted.append(' ');
            }
            formatted.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1).toLowerCase(Locale.ROOT));
        }

        return formatted.toString();
    }

    // -------------------------------------------------------------------------
    // Internal records
    // -------------------------------------------------------------------------

    private record ParsedRecognition(String intent, String entity) {
    }

    private record GeminiRecognitionResult(String intent, String entity) {
    }
}
