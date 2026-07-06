package com.exe.buddy_english_be.modules.buddy.service;

import com.exe.buddy_english_be.modules.buddy.dto.CreateBuddyProfileRequest;
import com.exe.buddy_english_be.modules.buddy.dto.BuddyProfileResponse;
import com.exe.buddy_english_be.modules.buddy.dto.UpdateBuddyProfileRequest;

import java.util.List;

public interface BuddyProfileService {
    BuddyProfileResponse createBuddyProfile(CreateBuddyProfileRequest request);
    BuddyProfileResponse getBuddyProfileById(Long id);
    List<BuddyProfileResponse> getAllBuddyProfiles();
    BuddyProfileResponse updateBuddyProfile(Long id, UpdateBuddyProfileRequest request);
    void deleteBuddyProfile(Long id);
}
