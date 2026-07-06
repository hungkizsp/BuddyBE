package com.exe.buddy_english_be.modules.buddy.service;

import com.exe.buddy_english_be.modules.buddy.dto.CreateBuddyCustomizationRequest;
import com.exe.buddy_english_be.modules.buddy.dto.BuddyCustomizationResponse;
import com.exe.buddy_english_be.modules.buddy.dto.UpdateBuddyCustomizationRequest;
import com.exe.buddy_english_be.modules.buddy.entity.BuddyCustomization;
import com.exe.buddy_english_be.modules.buddy.repository.BuddyCustomizationRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuddyCustomizationServiceImpl implements BuddyCustomizationService {

    private final BuddyCustomizationRepository buddyCustomizationRepository;

    @Override
    @Transactional
    public BuddyCustomizationResponse createBuddyCustomization(CreateBuddyCustomizationRequest request) {
        BuddyCustomization customization = BuddyCustomization.builder()
                .name(request.name())
                .type(request.type())
                .imageUrl(request.imageUrl())
                .price(request.price() != null ? request.price() : 0)
                .build();
        
        BuddyCustomization saved = buddyCustomizationRepository.save(customization);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BuddyCustomizationResponse getBuddyCustomizationById(Long id) {
        BuddyCustomization customization = buddyCustomizationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BUDDY_CUSTOMIZATION_NOT_FOUND));
        return mapToResponse(customization);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuddyCustomizationResponse> getAllBuddyCustomizations() {
        return buddyCustomizationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BuddyCustomizationResponse updateBuddyCustomization(Long id, UpdateBuddyCustomizationRequest request) {
        BuddyCustomization customization = buddyCustomizationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BUDDY_CUSTOMIZATION_NOT_FOUND));

        if (request.name() != null) customization.setName(request.name());
        if (request.type() != null) customization.setType(request.type());
        if (request.imageUrl() != null) customization.setImageUrl(request.imageUrl());
        if (request.price() != null) customization.setPrice(request.price());

        BuddyCustomization updated = buddyCustomizationRepository.save(customization);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteBuddyCustomization(Long id) {
        if (!buddyCustomizationRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.BUDDY_CUSTOMIZATION_NOT_FOUND);
        }
        buddyCustomizationRepository.deleteById(id);
    }

    private BuddyCustomizationResponse mapToResponse(BuddyCustomization customization) {
        return BuddyCustomizationResponse.builder()
                .id(customization.getId())
                .name(customization.getName())
                .type(customization.getType())
                .imageUrl(customization.getImageUrl())
                .price(customization.getPrice())
                .build();
    }
}
