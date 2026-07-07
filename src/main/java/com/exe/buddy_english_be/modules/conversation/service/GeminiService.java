package com.exe.buddy_english_be.modules.conversation.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.exe.buddy_english_be.shared.exception.GeminiQuotaExceededException;
import com.exe.buddy_english_be.shared.exception.GeminiUnavailableException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GeminiService {

    private final String apiKey;
    private final String apiUrl;
    private final String model;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public GeminiService(
            @Value("${gemini.api.key}") String apiKey,
            @Value("${gemini.api.url}") String apiUrl,
            @Value("${gemini.model}") String model) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.model = model;
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.httpClient = HttpClient.newBuilder().build();
    }

    /**
     * Calls the Gemini API and returns only valid text on success.
     *
     * @throws GeminiQuotaExceededException if the API returns HTTP 429 or RESOURCE_EXHAUSTED.
     * @throws GeminiUnavailableException   if the API key is missing, the API returns HTTP 5xx,
     *                                      a network/timeout error occurs, or no content is returned.
     */
    public String generateResponse(String systemInstruction, String userPrompt) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new GeminiUnavailableException("Gemini API key is not configured.");
        }

        try {
            String baseUrl = apiUrl.endsWith("/")
                    ? apiUrl.substring(0, apiUrl.length() - 1)
                    : apiUrl;
            String url = baseUrl + "/v1beta/models/" + model + ":generateContent?key=" + apiKey;

            GeminiRequest requestBody = new GeminiRequest(
                    List.of(new GeminiRequest.Content(List.of(new GeminiRequest.Part(userPrompt)))),
                    new GeminiRequest.SystemInstruction(List.of(new GeminiRequest.Part(systemInstruction))));

            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            log.info("Gemini API call status: {}", statusCode);

            if (statusCode == 429) {
                log.warn("Gemini quota exceeded (HTTP 429). Body: {}", response.body());
                throw new GeminiQuotaExceededException(
                        "Gemini API quota exceeded (HTTP 429). Please try again later.");
            }

            if (statusCode >= 500) {
                log.error("Gemini API server error (HTTP {}). Body: {}", statusCode, response.body());
                throw new GeminiUnavailableException(
                        "Gemini API is unavailable (HTTP " + statusCode + ").");
            }

            if (statusCode != 200) {
                log.error("Gemini API unexpected status (HTTP {}). Body: {}", statusCode, response.body());
                throw new GeminiUnavailableException(
                        "Gemini API returned an unexpected status code: " + statusCode);
            }

            GeminiResponse geminiResponse = objectMapper.readValue(response.body(), GeminiResponse.class);

            // Check for RESOURCE_EXHAUSTED in error response body
            if (response.body().contains("RESOURCE_EXHAUSTED")) {
                log.warn("Gemini quota exceeded (RESOURCE_EXHAUSTED in response body).");
                throw new GeminiQuotaExceededException(
                        "Gemini API quota exceeded (RESOURCE_EXHAUSTED).");
            }

            if (geminiResponse.candidates() != null && !geminiResponse.candidates().isEmpty()) {
                GeminiResponse.Candidate candidate = geminiResponse.candidates().get(0);
                if (candidate.content() != null && candidate.content().parts() != null
                        && !candidate.content().parts().isEmpty()) {
                    String text = candidate.content().parts().get(0).text();
                    if (text != null && !text.isBlank()) {
                        return text;
                    }
                }
            }

            log.warn("Gemini API returned a 200 response but with no usable content.");
            throw new GeminiUnavailableException("Gemini API returned no content in its response.");

        } catch (GeminiQuotaExceededException | GeminiUnavailableException e) {
            throw e;
        } catch (Exception e) {
            log.error("Network or unexpected error while calling Gemini API: {}", e.getMessage(), e);
            throw new GeminiUnavailableException(
                    "Failed to reach Gemini API due to a network or unexpected error.", e);
        }
    }

    public record GeminiRequest(
            List<Content> contents,
            SystemInstruction systemInstruction) {
        public record Content(
                List<Part> parts) {
        }

        public record SystemInstruction(
                List<Part> parts) {
        }

        public record Part(
                String text) {
        }
    }

    public record GeminiResponse(
            List<Candidate> candidates) {
        public record Candidate(
                Content content,
                String finishReason) {
        }

        public record Content(
                List<Part> parts,
                String role) {
        }

        public record Part(
                String text) {
        }
    }
}
