package com.exe.buddy_english_be.modules.profile.service;

import com.exe.buddy_english_be.modules.profile.dto.ChildProfileResponse;
import com.exe.buddy_english_be.modules.profile.dto.UpdateChildProfileRequest;
import com.exe.buddy_english_be.modules.profile.dto.ParentProfileResponse;
import com.exe.buddy_english_be.modules.profile.dto.UpdateParentProfileRequest;
import com.exe.buddy_english_be.modules.profile.dto.ParentChildrenResponse;

public interface ProfileService {

    // Child
    ChildProfileResponse getChildProfile(Long userId);
    ChildProfileResponse updateChildProfile(Long userId, UpdateChildProfileRequest request);

    // Parent
    ParentProfileResponse getParentProfile(Long userId);
    ParentProfileResponse updateParentProfile(Long userId, UpdateParentProfileRequest request);

    // Parent–child management
    ParentChildrenResponse getChildrenOfParent(Long parentUserId);
}
