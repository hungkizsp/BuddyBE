package com.exe.buddy_english_be.modules.progress.service;

import com.exe.buddy_english_be.modules.progress.dto.ChildScenarioProgressRequest;
import com.exe.buddy_english_be.modules.progress.dto.ChildScenarioProgressResponse;
import com.exe.buddy_english_be.modules.progress.dto.ChildVocabularyProgressRequest;
import com.exe.buddy_english_be.modules.progress.dto.ChildVocabularyProgressResponse;
import com.exe.buddy_english_be.modules.progress.dto.ChildWorldProgressRequest;
import com.exe.buddy_english_be.modules.progress.dto.ChildWorldProgressResponse;

import java.util.List;

public interface ProgressService {

    List<ChildVocabularyProgressResponse> getVocabularyProgressByChildId(Long childId);

    List<ChildVocabularyProgressResponse> getDueVocabularyProgress(Long childId);

    ChildVocabularyProgressResponse getVocabularyProgressById(Long id);

    ChildVocabularyProgressResponse createVocabularyProgress(ChildVocabularyProgressRequest request);

    ChildVocabularyProgressResponse updateVocabularyProgress(Long id, ChildVocabularyProgressRequest request);

    void deleteVocabularyProgress(Long id);

    List<ChildWorldProgressResponse> getWorldProgressByChildId(Long childId);

    ChildWorldProgressResponse getWorldProgressById(Long id);

    ChildWorldProgressResponse createWorldProgress(ChildWorldProgressRequest request);

    ChildWorldProgressResponse updateWorldProgress(Long id, ChildWorldProgressRequest request);

    void deleteWorldProgress(Long id);

    List<ChildScenarioProgressResponse> getScenarioProgressByChildId(Long childId);

    ChildScenarioProgressResponse getScenarioProgressById(Long id);

    ChildScenarioProgressResponse createScenarioProgress(ChildScenarioProgressRequest request);

    ChildScenarioProgressResponse updateScenarioProgress(Long id, ChildScenarioProgressRequest request);

    void deleteScenarioProgress(Long id);

}
