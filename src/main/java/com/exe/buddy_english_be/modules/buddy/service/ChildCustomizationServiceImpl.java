package com.exe.buddy_english_be.modules.buddy.service;

import com.exe.buddy_english_be.modules.buddy.dto.CreateChildCustomizationRequest;
import com.exe.buddy_english_be.modules.buddy.dto.ChildCustomizationResponse;
import com.exe.buddy_english_be.modules.buddy.dto.UpdateChildCustomizationRequest;
import com.exe.buddy_english_be.modules.buddy.entity.BuddyCustomization;
import com.exe.buddy_english_be.modules.buddy.entity.ChildCustomization;
import com.exe.buddy_english_be.modules.buddy.repository.BuddyCustomizationRepository;
import com.exe.buddy_english_be.modules.buddy.repository.ChildCustomizationRepository;
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
public class ChildCustomizationServiceImpl implements ChildCustomizationService {

    private final ChildCustomizationRepository childCustomizationRepository;
    private final ChildProfileRepository childProfileRepository;
    private final BuddyCustomizationRepository buddyCustomizationRepository;

    @Override
    @Transactional
    public ChildCustomizationResponse createChildCustomization(CreateChildCustomizationRequest request) {
        ChildProfile child = childProfileRepository.findById(request.childId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_PROFILE_NOT_FOUND));

        BuddyCustomization customization = buddyCustomizationRepository.findById(request.customizationId())
                .orElseThrow(() -> new BusinessException(ErrorCode.BUDDY_CUSTOMIZATION_NOT_FOUND));

        ChildCustomization childCustomization = ChildCustomization.builder()
                .child(child)
                .customization(customization)
                .equipped(request.equipped() != null ? request.equipped() : false)
                .obtainedAt(request.obtainedAt())
                .build();
        
        ChildCustomization saved = childCustomizationRepository.save(childCustomization);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ChildCustomizationResponse getChildCustomizationById(Long id) {
        ChildCustomization childCustomization = childCustomizationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_CUSTOMIZATION_NOT_FOUND));
        return mapToResponse(childCustomization);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChildCustomizationResponse> getAllChildCustomizations() {
        return childCustomizationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChildCustomizationResponse updateChildCustomization(Long id, UpdateChildCustomizationRequest request) {
        ChildCustomization childCustomization = childCustomizationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_CUSTOMIZATION_NOT_FOUND));

        if (request.equipped() != null) childCustomization.setEquipped(request.equipped());
        if (request.obtainedAt() != null) childCustomization.setObtainedAt(request.obtainedAt());

        ChildCustomization updated = childCustomizationRepository.save(childCustomization);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteChildCustomization(Long id) {
        if (!childCustomizationRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.CHILD_CUSTOMIZATION_NOT_FOUND);
        }
        childCustomizationRepository.deleteById(id);
    }

    private ChildCustomizationResponse mapToResponse(ChildCustomization childCustomization) {
        return ChildCustomizationResponse.builder()
                .id(childCustomization.getId())
                .childId(childCustomization.getChild().getId())
                .customizationId(childCustomization.getCustomization().getId())
                .equipped(childCustomization.getEquipped())
                .obtainedAt(childCustomization.getObtainedAt())
                .build();
    }
}
