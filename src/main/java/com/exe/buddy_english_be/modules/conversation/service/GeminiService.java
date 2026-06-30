package com.exe.buddy_english_be.modules.conversation.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public String generateResponse(String systemInstruction, String userPrompt) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return "WARNING_NO_API_KEY";
        }

        try {
            String baseUrl = apiUrl;
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
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
            log.info("Ai call status: {}, body: {}", response.statusCode(), response.body());
            if (response.statusCode() != 200) {
                System.err.println("Gemini API error. Status: " + response.statusCode() + ", Body: " + response.body());
                return "ERROR_API_FAILED: Status " + response.statusCode();
            }

            GeminiResponse geminiResponse = objectMapper.readValue(response.body(), GeminiResponse.class);
            if (geminiResponse.candidates() != null && !geminiResponse.candidates().isEmpty()) {
                GeminiResponse.Candidate candidate = geminiResponse.candidates().get(0);
                if (candidate.content() != null && candidate.content().parts() != null
                        && !candidate.content().parts().isEmpty()) {
                    return candidate.content().parts().get(0).text();
                }
            }

            return "ERROR_NO_CONTENT";
        } catch (Exception e) {
            System.err.println("Error calling Gemini API: " + e.getMessage());
            return "ERROR_EXCEPTION: " + e.getMessage();
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
