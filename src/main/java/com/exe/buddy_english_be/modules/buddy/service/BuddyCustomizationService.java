package com.exe.buddy_english_be.modules.buddy.service;

import com.exe.buddy_english_be.modules.buddy.dto.CreateBuddyCustomizationRequest;
import com.exe.buddy_english_be.modules.buddy.dto.BuddyCustomizationResponse;
import com.exe.buddy_english_be.modules.buddy.dto.UpdateBuddyCustomizationRequest;

import java.util.List;

public interface BuddyCustomizationService {
    BuddyCustomizationResponse createBuddyCustomization(CreateBuddyCustomizationRequest request);
    BuddyCustomizationResponse getBuddyCustomizationById(Long id);
    List<BuddyCustomizationResponse> getAllBuddyCustomizations();
    BuddyCustomizationResponse updateBuddyCustomization(Long id, UpdateBuddyCustomizationRequest request);
    void deleteBuddyCustomization(Long id);
}
