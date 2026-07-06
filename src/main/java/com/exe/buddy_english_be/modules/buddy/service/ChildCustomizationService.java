package com.exe.buddy_english_be.modules.buddy.service;

import com.exe.buddy_english_be.modules.buddy.dto.CreateChildCustomizationRequest;
import com.exe.buddy_english_be.modules.buddy.dto.ChildCustomizationResponse;
import com.exe.buddy_english_be.modules.buddy.dto.UpdateChildCustomizationRequest;

import java.util.List;

public interface ChildCustomizationService {
    ChildCustomizationResponse createChildCustomization(CreateChildCustomizationRequest request);
    ChildCustomizationResponse getChildCustomizationById(Long id);
    List<ChildCustomizationResponse> getAllChildCustomizations();
    ChildCustomizationResponse updateChildCustomization(Long id, UpdateChildCustomizationRequest request);
    void deleteChildCustomization(Long id);
}
