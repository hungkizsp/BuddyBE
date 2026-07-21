package com.exe.buddy_english_be.modules.character.service;

import com.exe.buddy_english_be.modules.character.dto.CharacterTaskResponse;
import com.exe.buddy_english_be.modules.character.dto.GenerateCharacterRequest;
import com.exe.buddy_english_be.modules.character.entity.CustomCharacter;
import com.exe.buddy_english_be.modules.character.enums.CharacterStatus;
import com.exe.buddy_english_be.modules.character.repository.CustomCharacterRepository;
import com.exe.buddy_english_be.modules.user.entity.User;
import com.exe.buddy_english_be.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CharacterService {

    private final CustomCharacterRepository characterRepository;
    private final UserRepository userRepository;
    private final MeshyApiClient meshyApiClient;

    @Value("${meshy.frontend.public-path}")
    private String frontendPublicPath;

    // ─── Generate ────────────────────────────────────────────────────────────────

    public CharacterTaskResponse generateCharacter(Long userId, GenerateCharacterRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // Submit task to Meshy
        String taskId = meshyApiClient.createTextTo3DTask(
                request.prompt(),
                request.artStyle(),
                null
        );

        if (taskId == null) {
            throw new RuntimeException("Failed to submit character generation task to Meshy API");
        }

        // Persist task record in DB
        CustomCharacter character = CustomCharacter.builder()
                .user(user)
                .taskId(taskId)
                .characterName(request.characterName() != null ? request.characterName() : "My Buddy")
                .prompt(request.prompt())
                .artStyle(request.artStyle())
                .status(CharacterStatus.PENDING)
                .progress(0)
                .build();

        character = characterRepository.save(character);
        log.info("Created character task {} for userId={}", taskId, userId);
        return toResponse(character);
    }

    // ─── Poll Task Status ─────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    public CharacterTaskResponse getTaskStatus(Long userId, Long characterId) {
        CustomCharacter character = characterRepository.findByIdAndUserId(characterId, userId)
                .orElseThrow(() -> new NoSuchElementException("Character task not found"));

        // If already in a terminal state, return from DB without calling Meshy
        if (character.getStatus() == CharacterStatus.SUCCEEDED
                || character.getStatus() == CharacterStatus.FAILED) {
            return toResponse(character);
        }

        // Poll Meshy for latest status
        Map<String, Object> meshyData = meshyApiClient.getTaskStatus(character.getTaskId());
        if (meshyData == null) {
            return toResponse(character);
        }

        String meshyStatus = String.valueOf(meshyData.getOrDefault("status", "PENDING"));
        int progress = parseProgress(meshyData.get("progress"));

        character.setProgress(progress);

        switch (meshyStatus) {
            case "SUCCEEDED" -> {
                character.setStatus(CharacterStatus.SUCCEEDED);
                character.setProgress(100);
                // Download the GLB file locally
                String glbUrl = extractGlbUrl(meshyData);
                String thumbnailUrl = extractThumbnailUrl(meshyData);
                if (glbUrl != null) {
                    String localPath = downloadGlbLocally(character.getTaskId(), glbUrl);
                    character.setLocalModelPath(localPath);
                }
                if (thumbnailUrl != null) {
                    character.setThumbnailUrl(thumbnailUrl);
                }
                log.info("Character task {} SUCCEEDED, saved to local path", character.getTaskId());
            }
            case "FAILED", "EXPIRED" -> {
                character.setStatus(CharacterStatus.FAILED);
                log.warn("Character task {} FAILED on Meshy side", character.getTaskId());
            }
            case "IN_PROGRESS" -> character.setStatus(CharacterStatus.IN_PROGRESS);
            default -> character.setStatus(CharacterStatus.PENDING);
        }

        character = characterRepository.save(character);
        return toResponse(character);
    }

    // ─── List user's characters ───────────────────────────────────────────────────

    public List<CharacterTaskResponse> getMyCharacters(Long userId) {
        return characterRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private String extractGlbUrl(Map<String, Object> meshyData) {
        try {
            Map<String, Object> modelUrls = (Map<String, Object>) meshyData.get("model_urls");
            if (modelUrls != null) {
                return String.valueOf(modelUrls.get("glb"));
            }
        } catch (Exception e) {
            log.warn("Could not extract GLB URL from Meshy response: {}", e.getMessage());
        }
        return null;
    }

    private String extractThumbnailUrl(Map<String, Object> meshyData) {
        try {
            Object thumbnail = meshyData.get("thumbnail_url");
            if (thumbnail != null) return String.valueOf(thumbnail);
        } catch (Exception e) {
            log.warn("Could not extract thumbnail URL: {}", e.getMessage());
        }
        return null;
    }

    private String downloadGlbLocally(String taskId, String glbUrl) {
        try {
            Path dir = Paths.get(frontendPublicPath);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            String filename = taskId + ".glb";
            Path destination = dir.resolve(filename);

            URL url = new URL(glbUrl);
            try (InputStream in = url.openStream()) {
                Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
            }

            // Return the relative path that Vite serves as a static file
            return "/custom_models/" + filename;
        } catch (Exception e) {
            log.error("Failed to download GLB for taskId={}: {}", taskId, e.getMessage());
            return null;
        }
    }

    private int parseProgress(Object rawProgress) {
        if (rawProgress instanceof Number number) {
            return number.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(rawProgress));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private CharacterTaskResponse toResponse(CustomCharacter c) {
        return new CharacterTaskResponse(
                c.getId(),
                c.getTaskId(),
                c.getCharacterName(),
                c.getPrompt(),
                c.getArtStyle(),
                c.getStatus(),
                c.getProgress(),
                c.getLocalModelPath(),
                c.getThumbnailUrl(),
                c.getCreatedAt()
        );
    }
}
