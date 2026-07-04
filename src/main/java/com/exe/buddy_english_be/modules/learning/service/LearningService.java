package com.exe.buddy_english_be.modules.learning.service;

import com.exe.buddy_english_be.modules.learning.dto.ScenarioRequest;
import com.exe.buddy_english_be.modules.learning.dto.ScenarioResponse;
import com.exe.buddy_english_be.modules.learning.dto.ScenarioStepRequest;
import com.exe.buddy_english_be.modules.learning.dto.ScenarioStepResponse;
import com.exe.buddy_english_be.modules.learning.dto.ScenarioVocabularyRequest;
import com.exe.buddy_english_be.modules.learning.dto.ScenarioVocabularyResponse;
import com.exe.buddy_english_be.modules.learning.dto.WorldRequest;
import com.exe.buddy_english_be.modules.learning.dto.WorldResponse;

import java.util.List;

public interface LearningService {

    List<WorldResponse> getAllWorlds();

    List<WorldResponse> getActiveWorlds();

    WorldResponse getWorldById(Long id);

    WorldResponse createWorld(WorldRequest request);

    WorldResponse updateWorld(Long id, WorldRequest request);

    void deleteWorld(Long id);

    List<ScenarioResponse> getAllScenarios();

    List<ScenarioResponse> getScenariosByWorldId(Long worldId);

    ScenarioResponse getScenarioById(Long id);

    ScenarioResponse createScenario(ScenarioRequest request);

    ScenarioResponse updateScenario(Long id, ScenarioRequest request);

    void deleteScenario(Long id);

    List<ScenarioStepResponse> getAllScenarioSteps();

    List<ScenarioStepResponse> getScenarioStepsByScenarioId(Long scenarioId);

    ScenarioStepResponse getScenarioStepById(Long id);

    ScenarioStepResponse createScenarioStep(ScenarioStepRequest request);

    ScenarioStepResponse updateScenarioStep(Long id, ScenarioStepRequest request);

    void deleteScenarioStep(Long id);

    List<ScenarioVocabularyResponse> getAllScenarioVocabularies();

    List<ScenarioVocabularyResponse> getScenarioVocabulariesByScenarioId(Long scenarioId);

    ScenarioVocabularyResponse getScenarioVocabularyById(Long id);

    ScenarioVocabularyResponse createScenarioVocabulary(ScenarioVocabularyRequest request);

    void deleteScenarioVocabulary(Long id);
}
