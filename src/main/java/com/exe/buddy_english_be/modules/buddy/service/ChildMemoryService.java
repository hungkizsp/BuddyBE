package com.exe.buddy_english_be.modules.buddy.service;

import com.exe.buddy_english_be.modules.buddy.dto.CreateChildMemoryRequest;
import com.exe.buddy_english_be.modules.buddy.dto.ChildMemoryResponse;
import com.exe.buddy_english_be.modules.buddy.dto.UpdateChildMemoryRequest;

import java.util.List;

public interface ChildMemoryService {
    ChildMemoryResponse createChildMemory(CreateChildMemoryRequest request);
    ChildMemoryResponse getChildMemoryById(Long id);
    List<ChildMemoryResponse> getAllChildMemories();
    ChildMemoryResponse updateChildMemory(Long id, UpdateChildMemoryRequest request);
    void deleteChildMemory(Long id);
}
