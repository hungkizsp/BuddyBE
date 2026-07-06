package com.exe.buddy_english_be.modules.buddy.service;

import com.exe.buddy_english_be.modules.buddy.dto.CreateChildMemoryRequest;
import com.exe.buddy_english_be.modules.buddy.dto.ChildMemoryResponse;
import com.exe.buddy_english_be.modules.buddy.dto.UpdateChildMemoryRequest;
import com.exe.buddy_english_be.modules.buddy.entity.ChildMemory;
import com.exe.buddy_english_be.modules.buddy.repository.ChildMemoryRepository;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChildMemoryServiceImpl implements ChildMemoryService {

    private final ChildMemoryRepository childMemoryRepository;
    private final ChildProfileRepository childProfileRepository;

    @Override
    @Transactional
    public ChildMemoryResponse createChildMemory(CreateChildMemoryRequest request) {
        ChildProfile child = childProfileRepository.findById(request.childId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_PROFILE_NOT_FOUND));

        ChildMemory childMemory = ChildMemory.builder()
                .child(child)
                .memoryType(request.memoryType())
                .memoryKey(request.memoryKey())
                .memoryValue(request.memoryValue())
                .confidence(request.confidence())
                .source(request.source())
                .importance(request.importance() != null ? request.importance() : 1)
                .isActive(request.isActive() != null ? request.isActive() : true)
                .lastUsedAt(request.lastUsedAt())
                .build();
        
        ChildMemory saved = childMemoryRepository.save(childMemory);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ChildMemoryResponse getChildMemoryById(Long id) {
        ChildMemory childMemory = childMemoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_MEMORY_NOT_FOUND));
        return mapToResponse(childMemory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChildMemoryResponse> getAllChildMemories() {
        return childMemoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChildMemoryResponse updateChildMemory(Long id, UpdateChildMemoryRequest request) {
        ChildMemory childMemory = childMemoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_MEMORY_NOT_FOUND));

        if (request.memoryType() != null) childMemory.setMemoryType(request.memoryType());
        if (request.memoryKey() != null) childMemory.setMemoryKey(request.memoryKey());
        if (request.memoryValue() != null) childMemory.setMemoryValue(request.memoryValue());
        if (request.confidence() != null) childMemory.setConfidence(request.confidence());
        if (request.source() != null) childMemory.setSource(request.source());
        if (request.importance() != null) childMemory.setImportance(request.importance());
        if (request.isActive() != null) childMemory.setIsActive(request.isActive());
        if (request.lastUsedAt() != null) childMemory.setLastUsedAt(request.lastUsedAt());

        ChildMemory updated = childMemoryRepository.save(childMemory);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteChildMemory(Long id) {
        if (!childMemoryRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.CHILD_MEMORY_NOT_FOUND);
        }
        childMemoryRepository.deleteById(id);
    }

    private ChildMemoryResponse mapToResponse(ChildMemory childMemory) {
        return ChildMemoryResponse.builder()
                .id(childMemory.getId())
                .childId(childMemory.getChild().getId())
                .memoryType(childMemory.getMemoryType())
                .memoryKey(childMemory.getMemoryKey())
                .memoryValue(childMemory.getMemoryValue())
                .confidence(childMemory.getConfidence())
                .source(childMemory.getSource())
                .importance(childMemory.getImportance())
                .isActive(childMemory.getIsActive())
                .lastUsedAt(childMemory.getLastUsedAt())
                .build();
    }
}
