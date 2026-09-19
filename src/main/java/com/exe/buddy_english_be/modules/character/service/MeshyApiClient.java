package com.exe.buddy_english_be.modules.character.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Low-level client to communicate with Meshy.ai REST API.
 * Docs: https://docs.meshy.ai/api-text-to-3d
 */
@Service
@Slf4j
public class MeshyApiClient {

    @Value("${meshy.api.key}")
    private String apiKey;

    @Value("${meshy.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Step 1: Submit a Text-to-3D generation task to Meshy.
     * Returns the task ID string on success.
     */
    @SuppressWarnings("unchecked")
    public String createTextTo3DTask(String prompt, String artStyle, String negativePrompt) {
        String url = apiUrl + "/v2/text-to-3d";

        HttpHeaders headers = buildHeaders();
        Map<String, Object> body = Map.of(
                "mode", "preview",
                "prompt", prompt,
                "art_style", artStyle,
                "negative_prompt", negativePrompt != null ? negativePrompt : "ugly, deformed, low quality, NSFW",
                "ai_model", "meshy-6"
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("result")) {
                    return String.valueOf(responseBody.get("result"));
                }
            }
            log.error("Unexpected Meshy response status: {}", response.getStatusCode());
            return null;
        } catch (Exception e) {
            log.error("Failed to create Meshy Text-to-3D task: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Step 2: Poll for task status and progress.
     * Returns a map containing: status, progress, model_urls (if done).
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getTaskStatus(String taskId) {
        String url = apiUrl + "/v2/text-to-3d/" + taskId;

        HttpHeaders headers = buildHeaders();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to get Meshy task status for taskId={}: {}", taskId, e.getMessage());
            return null;
        }
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        return headers;
    }
}
