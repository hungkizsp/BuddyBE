package com.exe.buddy_english_be.modules.learning.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exe.buddy_english_be.modules.learning.dto.ScenarioRequest;
import com.exe.buddy_english_be.modules.learning.dto.ScenarioResponse;
import com.exe.buddy_english_be.modules.learning.dto.ScenarioStepRequest;
import com.exe.buddy_english_be.modules.learning.dto.ScenarioStepResponse;
import com.exe.buddy_english_be.modules.learning.dto.ScenarioVocabularyRequest;
import com.exe.buddy_english_be.modules.learning.dto.ScenarioVocabularyResponse;
import com.exe.buddy_english_be.modules.learning.dto.WorldRequest;
import com.exe.buddy_english_be.modules.learning.dto.WorldResponse;
import com.exe.buddy_english_be.modules.learning.entity.Scenario;
import com.exe.buddy_english_be.modules.learning.entity.ScenarioStep;
import com.exe.buddy_english_be.modules.learning.entity.ScenarioVocabulary;
import com.exe.buddy_english_be.modules.learning.entity.World;
import com.exe.buddy_english_be.modules.learning.repository.ScenarioRepository;
import com.exe.buddy_english_be.modules.learning.repository.ScenarioStepRepository;
import com.exe.buddy_english_be.modules.learning.repository.ScenarioVocabularyRepository;
import com.exe.buddy_english_be.modules.learning.repository.WorldRepository;
import com.exe.buddy_english_be.modules.vocabulary.entity.Vocabulary;
import com.exe.buddy_english_be.modules.vocabulary.repository.VocabularyRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;

@Service
public class LearningServiceImpl implements LearningService {
    private final WorldRepository worldRepository;
    private final ScenarioRepository scenarioRepository;
    private final ScenarioStepRepository scenarioStepRepository;
    private final ScenarioVocabularyRepository scenarioVocabularyRepository;
    private final VocabularyRepository vocabularyRepository;

    public LearningServiceImpl(
            WorldRepository worldRepository,
            ScenarioRepository scenarioRepository,
            ScenarioStepRepository scenarioStepRepository,
            ScenarioVocabularyRepository scenarioVocabularyRepository,
            VocabularyRepository vocabularyRepository) {
        this.worldRepository = worldRepository;
        this.scenarioRepository = scenarioRepository;
        this.scenarioStepRepository = scenarioStepRepository;
        this.scenarioVocabularyRepository = scenarioVocabularyRepository;
        this.vocabularyRepository = vocabularyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorldResponse> getAllWorlds() {
        return worldRepository.findAll().stream()
                .map(this::toWorldResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorldResponse> getActiveWorlds() {
        return worldRepository.findByIsActiveTrueOrderByOrderIndexAsc().stream()
                .map(this::toWorldResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public WorldResponse getWorldById(Long id) {
        return toWorldResponse(findWorld(id));
    }

    @Override
    @Transactional
    public WorldResponse createWorld(WorldRequest request) {
        World world = World.builder()
                .name(request.name().trim())
                .description(normalize(request.description()))
                .thumbnail(normalize(request.thumbnail()))
                .orderIndex(defaultInteger(request.orderIndex(), 0))
                .isActive(defaultBoolean(request.isActive(), true))
                .build();

        return toWorldResponse(worldRepository.save(world));
    }

    @Override
    @Transactional
    public WorldResponse updateWorld(Long id, WorldRequest request) {
        World world = findWorld(id);
        world.setName(request.name().trim());
        world.setDescription(normalize(request.description()));
        world.setThumbnail(normalize(request.thumbnail()));
        world.setOrderIndex(defaultInteger(request.orderIndex(), 0));
        world.setIsActive(defaultBoolean(request.isActive(), true));

        return toWorldResponse(worldRepository.save(world));
    }

    @Override
    @Transactional
    public void deleteWorld(Long id) {
        worldRepository.delete(findWorld(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScenarioResponse> getAllScenarios() {
        return scenarioRepository.findAll().stream()
                .map(this::toScenarioResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScenarioResponse> getScenariosByWorldId(Long worldId) {
        findWorld(worldId);
        return scenarioRepository.findByWorldIdOrderByOrderIndexAsc(worldId).stream()
                .map(this::toScenarioResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ScenarioResponse getScenarioById(Long id) {
        return toScenarioResponse(findScenario(id));
    }

    @Override
    @Transactional
    public ScenarioResponse createScenario(ScenarioRequest request) {
        Scenario scenario = Scenario.builder()
                .world(findWorld(request.worldId()))
                .title(request.title().trim())
                .description(normalize(request.description()))
                .scenarioType(normalize(request.scenarioType()))
                .expectedIntent(normalize(request.expectedIntent()))
                .difficulty(normalize(request.difficulty()))
                .orderIndex(defaultInteger(request.orderIndex(), 0))
                .build();

        return toScenarioResponse(scenarioRepository.save(scenario));
    }

    @Override
    @Transactional
    public ScenarioResponse updateScenario(Long id, ScenarioRequest request) {
        Scenario scenario = findScenario(id);
        scenario.setWorld(findWorld(request.worldId()));
        scenario.setTitle(request.title().trim());
        scenario.setDescription(normalize(request.description()));
        scenario.setScenarioType(normalize(request.scenarioType()));
        scenario.setExpectedIntent(normalize(request.expectedIntent()));
        scenario.setDifficulty(normalize(request.difficulty()));
        scenario.setOrderIndex(defaultInteger(request.orderIndex(), 0));

        return toScenarioResponse(scenarioRepository.save(scenario));
    }

    @Override
    @Transactional
    public void deleteScenario(Long id) {
        scenarioRepository.delete(findScenario(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScenarioStepResponse> getAllScenarioSteps() {
        return scenarioStepRepository.findAll().stream()
                .map(this::toScenarioStepResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScenarioStepResponse> getScenarioStepsByScenarioId(Long scenarioId) {
        findScenario(scenarioId);
        return scenarioStepRepository.findByScenarioIdOrderByStepOrderAsc(scenarioId).stream()
                .map(this::toScenarioStepResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ScenarioStepResponse getScenarioStepById(Long id) {
        return toScenarioStepResponse(findScenarioStep(id));
    }

    @Override
    @Transactional
    public ScenarioStepResponse createScenarioStep(ScenarioStepRequest request) {
        ScenarioStep step = ScenarioStep.builder()
                .scenario(findScenario(request.scenarioId()))
                .stepOrder(request.stepOrder())
                .buddyMessage(normalize(request.buddyMessage()))
                .expectedIntent(normalize(request.expectedIntent()))
                .expectedEntity(normalize(request.expectedEntity()))
                .successResponse(normalize(request.successResponse()))
                .failResponse(normalize(request.failResponse()))
                .nextStep(findScenarioStepOrNull(request.nextStepId()))
                .build();

        return toScenarioStepResponse(scenarioStepRepository.save(step));
    }

    @Override
    @Transactional
    public ScenarioStepResponse updateScenarioStep(Long id, ScenarioStepRequest request) {
        ScenarioStep step = findScenarioStep(id);
        step.setScenario(findScenario(request.scenarioId()));
        step.setStepOrder(request.stepOrder());
        step.setBuddyMessage(normalize(request.buddyMessage()));
        step.setExpectedIntent(normalize(request.expectedIntent()));
        step.setExpectedEntity(normalize(request.expectedEntity()));
        step.setSuccessResponse(normalize(request.successResponse()));
        step.setFailResponse(normalize(request.failResponse()));
        step.setNextStep(findScenarioStepOrNull(request.nextStepId()));

        return toScenarioStepResponse(scenarioStepRepository.save(step));
    }

    @Override
    @Transactional
    public void deleteScenarioStep(Long id) {
        scenarioStepRepository.delete(findScenarioStep(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScenarioVocabularyResponse> getAllScenarioVocabularies() {
        return scenarioVocabularyRepository.findAll().stream()
                .map(this::toScenarioVocabularyResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScenarioVocabularyResponse> getScenarioVocabulariesByScenarioId(Long scenarioId) {
        findScenario(scenarioId);
        return scenarioVocabularyRepository.findByScenarioId(scenarioId).stream()
                .map(this::toScenarioVocabularyResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ScenarioVocabularyResponse getScenarioVocabularyById(Long id) {
        return toScenarioVocabularyResponse(findScenarioVocabulary(id));
    }

    @Override
    @Transactional
    public ScenarioVocabularyResponse createScenarioVocabulary(ScenarioVocabularyRequest request) {
        Scenario scenario = findScenario(request.scenarioId());
        Vocabulary vocabulary = findVocabulary(request.vocabularyId());

        ScenarioVocabulary scenarioVocabulary = scenarioVocabularyRepository
                .findByScenarioIdAndVocabularyId(request.scenarioId(), request.vocabularyId())
                .orElseGet(() -> ScenarioVocabulary.builder()
                        .scenario(scenario)
                        .vocabulary(vocabulary)
                        .build());

        return toScenarioVocabularyResponse(scenarioVocabularyRepository.save(scenarioVocabulary));
    }

    @Override
    @Transactional
    public void deleteScenarioVocabulary(Long id) {
        scenarioVocabularyRepository.delete(findScenarioVocabulary(id));
    }

    private World findWorld(Long id) {
        return worldRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.WORLD_NOT_FOUND));
    }

    private Scenario findScenario(Long id) {
        return scenarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SCENARIO_NOT_FOUND));
    }

    private ScenarioStep findScenarioStep(Long id) {
        return scenarioStepRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SCENARIO_STEP_NOT_FOUND));
    }

    private ScenarioStep findScenarioStepOrNull(Long id) {
        if (id == null) {
            return null;
        }
        return findScenarioStep(id);
    }

    private ScenarioVocabulary findScenarioVocabulary(Long id) {
        return scenarioVocabularyRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SCENARIO_VOCABULARY_NOT_FOUND));
    }

    private Vocabulary findVocabulary(Long id) {
        return vocabularyRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.VOCABULARY_NOT_FOUND));
    }

    private WorldResponse toWorldResponse(World world) {
        return new WorldResponse(
                world.getId(),
                world.getName(),
                world.getDescription(),
                world.getThumbnail(),
                world.getOrderIndex(),
                world.getIsActive(),
                world.getCreatedAt(),
                world.getUpdatedAt()
        );
    }

    private ScenarioResponse toScenarioResponse(Scenario scenario) {
        World world = scenario.getWorld();
        return new ScenarioResponse(
                scenario.getId(),
                world.getId(),
                world.getName(),
                scenario.getTitle(),
                scenario.getDescription(),
                scenario.getScenarioType(),
                scenario.getExpectedIntent(),
                scenario.getDifficulty(),
                scenario.getOrderIndex(),
                scenario.getCreatedAt(),
                scenario.getUpdatedAt()
        );
    }

    private ScenarioStepResponse toScenarioStepResponse(ScenarioStep step) {
        Scenario scenario = step.getScenario();
        ScenarioStep nextStep = step.getNextStep();
        return new ScenarioStepResponse(
                step.getId(),
                scenario.getId(),
                scenario.getTitle(),
                step.getStepOrder(),
                step.getBuddyMessage(),
                step.getExpectedIntent(),
                step.getExpectedEntity(),
                step.getSuccessResponse(),
                step.getFailResponse(),
                nextStep != null ? nextStep.getId() : null,
                step.getCreatedAt(),
                step.getUpdatedAt()
        );
    }

    private ScenarioVocabularyResponse toScenarioVocabularyResponse(ScenarioVocabulary scenarioVocabulary) {
        Scenario scenario = scenarioVocabulary.getScenario();
        Vocabulary vocabulary = scenarioVocabulary.getVocabulary();
        return new ScenarioVocabularyResponse(
                scenarioVocabulary.getId(),
                scenario.getId(),
                scenario.getTitle(),
                vocabulary.getId(),
                vocabulary.getWord(),
                vocabulary.getPhonetic(),
                vocabulary.getMeaning(),
                vocabulary.getExampleSentence(),
                vocabulary.getImageUrl(),
                vocabulary.getAudioUrl(),
                vocabulary.getDifficulty(),
                scenarioVocabulary.getCreatedAt(),
                scenarioVocabulary.getUpdatedAt()
        );
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private Integer defaultInteger(Integer value, Integer defaultValue) {
        return value == null ? defaultValue : value;
    }

    private Boolean defaultBoolean(Boolean value, Boolean defaultValue) {
        return value == null ? defaultValue : value;
    }
}
