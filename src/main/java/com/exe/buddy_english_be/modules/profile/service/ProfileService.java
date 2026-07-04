package com.exe.buddy_english_be.modules.profile.service;

import com.exe.buddy_english_be.modules.profile.dto.ChildProfileResponse;
import com.exe.buddy_english_be.modules.profile.dto.CreateChildProfileRequest;
import com.exe.buddy_english_be.modules.profile.dto.UpdateChildProfileRequest;
import com.exe.buddy_english_be.modules.profile.dto.CreateParentProfileRequest;
import com.exe.buddy_english_be.modules.profile.dto.ParentProfileResponse;
import com.exe.buddy_english_be.modules.profile.dto.UpdateParentProfileRequest;
import com.exe.buddy_english_be.modules.profile.dto.ParentChildrenResponse;

import java.util.List;

public interface ProfileService {

    // ─── Child ───────────────────────────────────────────────────────────────

    /** Get all child profiles (admin). */
    List<ChildProfileResponse> getAllChildProfiles();

    /** Get a child profile by profile ID (admin). */
    ChildProfileResponse getChildProfileById(Long id);

    /** Get a child profile by userId (self). */
    ChildProfileResponse getChildProfile(Long userId);

    /** Create a new child profile (admin). */
    ChildProfileResponse createChildProfile(CreateChildProfileRequest request);

    /** Update child profile fields (self). */
    ChildProfileResponse updateChildProfile(Long userId, UpdateChildProfileRequest request);

    /** Update child profile by profile ID (admin). */
    ChildProfileResponse updateChildProfileById(Long id, UpdateChildProfileRequest request);

    /** Delete a child profile by profile ID (admin). */
    void deleteChildProfile(Long id);

    // ─── Parent ──────────────────────────────────────────────────────────────

    /** Get all parent profiles (admin). */
    List<ParentProfileResponse> getAllParentProfiles();

    /** Get a parent profile by profile ID (admin). */
    ParentProfileResponse getParentProfileById(Long id);

    /** Get a parent profile by userId (self). */
    ParentProfileResponse getParentProfile(Long userId);

    /** Create a new parent profile (admin). */
    ParentProfileResponse createParentProfile(CreateParentProfileRequest request);

    /** Update parent profile fields (self). */
    ParentProfileResponse updateParentProfile(Long userId, UpdateParentProfileRequest request);

    /** Update parent profile by profile ID (admin). */
    ParentProfileResponse updateParentProfileById(Long id, UpdateParentProfileRequest request);

    /** Delete a parent profile by profile ID (admin). */
    void deleteParentProfile(Long id);

    // ─── Parent–child management ──────────────────────────────────────────────

    /** Get children of the authenticated parent (self). */
    ParentChildrenResponse getChildrenOfParent(Long parentUserId);

    /** Get children of a parent by parent profile ID (admin). */
    ParentChildrenResponse getChildrenOfParentById(Long parentProfileId);
}
