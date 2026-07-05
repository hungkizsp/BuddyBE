package com.exe.buddy_english_be.modules.speech.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.exe.buddy_english_be.modules.conversation.service.GeminiService;
import com.exe.buddy_english_be.modules.speech.dto.SpeechEvaluationRequest;
import com.exe.buddy_english_be.modules.speech.dto.SpeechEvaluationResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpeechEvaluationServiceImpl implements SpeechEvaluationService {

    private final GeminiService geminiService;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final String SYSTEM_PROMPT = """
            You are an experienced English pronunciation teacher for children.

            Analyze pronunciation objectively.
            Do not invent mistakes.
            Base every conclusion on the provided pronunciation scores.
            Explain mistakes simply.
            Praise good pronunciation.
            If a word has accuracy below 80, explain how to improve.
            If a phoneme accuracy is below 70, explain which sound should be practiced.
            If prosody is low, explain how to improve intonation naturally.
            Keep explanations friendly.
            Return ONLY valid JSON.
            
            Do not include any markdown format, do not wrap the JSON in ```json ``` code blocks.
            
            Response schema:
            {
              "overallFeedback": "overall feedback string",
              "strengths": ["strength 1", "strength 2"],
              "weaknesses": ["weakness 1", "weakness 2"],
              "improvementTips": ["tip 1", "tip 2"],
              "wordFeedback": [
                {
                  "word": "word string",
                  "problem": "problem description",
                  "tip": "practice tip"
                }
              ]
            }
            """;

    @Override
    public SpeechEvaluationResponse evaluate(Long userId, SpeechEvaluationRequest request) {
        try {
            String userPrompt = objectMapper.writeValueAsString(request);
            log.info("Sending request to Gemini for userId: {}", userId);
            
            String responseStr = geminiService.generateResponse(SYSTEM_PROMPT, userPrompt);
            log.info("Received raw response from Gemini: {}", responseStr);

            if (responseStr == null || responseStr.contains("ERROR_") || responseStr.contains("WARNING_")) {
                return getFallbackResponse();
            }

            String cleanedJson = cleanJson(responseStr);
            return objectMapper.readValue(cleanedJson, SpeechEvaluationResponse.class);

        } catch (Exception e) {
            log.error("Error evaluating speech with Gemini, returning fallback response", e);
            return getFallbackResponse();
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

    private SpeechEvaluationResponse getFallbackResponse() {
        return SpeechEvaluationResponse.builder()
                .overallFeedback("Great job trying to read the sentence! Keep practicing to make your English sound even more natural.")
                .strengths(List.of("Wonderful effort in speaking and practicing!"))
                .weaknesses(List.of("Some words or sounds can be spoken more clearly."))
                .improvementTips(List.of(
                        "Try repeating the words slowly after listening to them.",
                        "Practice reading along with your AI buddy often!"))
                .wordFeedback(List.of())
                .build();
    }
}
