package com.exe.buddy_english_be.modules.profile.service;

import com.exe.buddy_english_be.modules.profile.dto.ChildProfileResponse;
import com.exe.buddy_english_be.modules.profile.dto.CreateChildProfileRequest;
import com.exe.buddy_english_be.modules.profile.dto.UpdateChildProfileRequest;
import com.exe.buddy_english_be.modules.profile.dto.CreateParentProfileRequest;
import com.exe.buddy_english_be.modules.profile.dto.ParentChildrenResponse;
import com.exe.buddy_english_be.modules.profile.dto.ParentProfileResponse;
import com.exe.buddy_english_be.modules.profile.dto.UpdateParentProfileRequest;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.profile.entity.ParentChild;
import com.exe.buddy_english_be.modules.profile.entity.ParentProfile;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import com.exe.buddy_english_be.modules.profile.repository.ParentChildRepository;
import com.exe.buddy_english_be.modules.profile.repository.ParentProfileRepository;
import com.exe.buddy_english_be.modules.user.entity.User;
import com.exe.buddy_english_be.modules.user.repository.UserRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ChildProfileRepository childProfileRepository;
    private final ParentProfileRepository parentProfileRepository;
    private final ParentChildRepository parentChildRepository;
    private final UserRepository userRepository;

    // ─── Child ───────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<ChildProfileResponse> getAllChildProfiles() {
        log.info("Fetching all child profiles");
        return childProfileRepository.findAll()
                .stream()
                .map(this::mapToChildResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ChildProfileResponse getChildProfileById(Long id) {
        log.info("Fetching child profile by id={}", id);
        ChildProfile child = childProfileRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_PROFILE_NOT_FOUND));
        return mapToChildResponse(child);
    }

    @Override
    @Transactional(readOnly = true)
    public ChildProfileResponse getChildProfile(Long userId) {
        log.info("Fetching child profile for userId={}", userId);
        ChildProfile child = childProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_PROFILE_NOT_FOUND));
        return mapToChildResponse(child);
    }

    @Override
    @Transactional
    public ChildProfileResponse createChildProfile(CreateChildProfileRequest request) {
        log.info("Creating child profile for userId={}", request.userId());
        if (childProfileRepository.existsByUserId(request.userId())) {
            throw new BusinessException(ErrorCode.PROFILE_ALREADY_EXISTS);
        }
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        ChildProfile child = ChildProfile.builder()
                .user(user)
                .nickname(request.nickname())
                .avatarUrl(request.avatarUrl())
                .birthDate(request.birthDate())
                .gender(request.gender())
                .build();

        childProfileRepository.save(child);
        return mapToChildResponse(child);
    }

    @Override
    @Transactional
    public ChildProfileResponse updateChildProfile(Long userId, UpdateChildProfileRequest request) {
        log.info("Updating child profile for userId={}", userId);
        ChildProfile child = childProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_PROFILE_NOT_FOUND));
        applyChildUpdates(child, request);
        childProfileRepository.save(child);
        return mapToChildResponse(child);
    }

    @Override
    @Transactional
    public ChildProfileResponse updateChildProfileById(Long id, UpdateChildProfileRequest request) {
        log.info("Updating child profile by id={}", id);
        ChildProfile child = childProfileRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_PROFILE_NOT_FOUND));
        applyChildUpdates(child, request);
        childProfileRepository.save(child);
        return mapToChildResponse(child);
    }

    @Override
    @Transactional
    public void deleteChildProfile(Long id) {
        log.info("Deleting child profile id={}", id);
        if (!childProfileRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.CHILD_PROFILE_NOT_FOUND);
        }
        childProfileRepository.deleteById(id);
    }

    // ─── Parent ──────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<ParentProfileResponse> getAllParentProfiles() {
        log.info("Fetching all parent profiles");
        return parentProfileRepository.findAll()
                .stream()
                .map(this::mapToParentResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ParentProfileResponse getParentProfileById(Long id) {
        log.info("Fetching parent profile by id={}", id);
        ParentProfile parent = parentProfileRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PARENT_PROFILE_NOT_FOUND));
        return mapToParentResponse(parent);
    }

    @Override
    @Transactional(readOnly = true)
    public ParentProfileResponse getParentProfile(Long userId) {
        log.info("Fetching parent profile for userId={}", userId);
        ParentProfile parent = parentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PARENT_PROFILE_NOT_FOUND));
        return mapToParentResponse(parent);
    }

    @Override
    @Transactional
    public ParentProfileResponse createParentProfile(CreateParentProfileRequest request) {
        log.info("Creating parent profile for userId={}", request.userId());
        if (parentProfileRepository.findByUserId(request.userId()).isPresent()) {
            throw new BusinessException(ErrorCode.PROFILE_ALREADY_EXISTS);
        }
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        ParentProfile parent = ParentProfile.builder()
                .user(user)
                .fullName(request.fullName())
                .phone(request.phone())
                .occupation(request.occupation())
                .avatarUrl(request.avatarUrl())
                .build();

        parentProfileRepository.save(parent);
        return mapToParentResponse(parent);
    }

    @Override
    @Transactional
    public ParentProfileResponse updateParentProfile(Long userId, UpdateParentProfileRequest request) {
        log.info("Updating parent profile for userId={}", userId);
        ParentProfile parent = parentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PARENT_PROFILE_NOT_FOUND));
        applyParentUpdates(parent, request);
        parentProfileRepository.save(parent);
        return mapToParentResponse(parent);
    }

    @Override
    @Transactional
    public ParentProfileResponse updateParentProfileById(Long id, UpdateParentProfileRequest request) {
        log.info("Updating parent profile by id={}", id);
        ParentProfile parent = parentProfileRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PARENT_PROFILE_NOT_FOUND));
        applyParentUpdates(parent, request);
        parentProfileRepository.save(parent);
        return mapToParentResponse(parent);
    }

    @Override
    @Transactional
    public void deleteParentProfile(Long id) {
        log.info("Deleting parent profile id={}", id);
        if (!parentProfileRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.PARENT_PROFILE_NOT_FOUND);
        }
        parentProfileRepository.deleteById(id);
    }

    // ─── Parent–Child ────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public ParentChildrenResponse getChildrenOfParent(Long parentUserId) {
        log.info("Fetching children for parentUserId={}", parentUserId);
        ParentProfile parent = parentProfileRepository.findByUserId(parentUserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PARENT_PROFILE_NOT_FOUND));
        return buildParentChildrenResponse(parent);
    }

    @Override
    @Transactional(readOnly = true)
    public ParentChildrenResponse getChildrenOfParentById(Long parentProfileId) {
        log.info("Fetching children for parentProfileId={}", parentProfileId);
        ParentProfile parent = parentProfileRepository.findById(parentProfileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PARENT_PROFILE_NOT_FOUND));
        return buildParentChildrenResponse(parent);
    }

    // ─── Private helpers ─────────────────────────────────────────────────────

    private void applyChildUpdates(ChildProfile child, UpdateChildProfileRequest request) {
        if (request.nickname() != null) child.setNickname(request.nickname());
        if (request.avatarUrl() != null) child.setAvatarUrl(request.avatarUrl());
        if (request.birthDate() != null) child.setBirthDate(request.birthDate());
        if (request.gender() != null) child.setGender(request.gender());
    }

    private void applyParentUpdates(ParentProfile parent, UpdateParentProfileRequest request) {
        if (request.fullName() != null) parent.setFullName(request.fullName());
        if (request.phone() != null) parent.setPhone(request.phone());
        if (request.occupation() != null) parent.setOccupation(request.occupation());
        if (request.avatarUrl() != null) parent.setAvatarUrl(request.avatarUrl());
    }

    private ParentChildrenResponse buildParentChildrenResponse(ParentProfile parent) {
        List<ParentChildrenResponse.ChildSummary> children = parentChildRepository
                .findByParentId(parent.getId())
                .stream()
                .map(this::mapToChildSummary)
                .toList();

        return ParentChildrenResponse.builder()
                .parentId(parent.getId())
                .children(children)
                .build();
    }

    // ─── Mappers ─────────────────────────────────────────────────────────────

    private ChildProfileResponse mapToChildResponse(ChildProfile child) {
        return ChildProfileResponse.builder()
                .id(child.getId())
                .userId(child.getUser() != null ? child.getUser().getId() : null)
                .nickname(child.getNickname())
                .avatarUrl(child.getAvatarUrl())
                .birthDate(child.getBirthDate())
                .gender(child.getGender())
                .level(child.getLevel())
                .xp(child.getXp())
                .coins(child.getCoins())
                .streakDays(child.getStreakDays())
                .lastLoginDate(child.getLastLoginDate())
                .lastSessionAt(child.getLastSessionAt())
                .build();
    }

    private ParentProfileResponse mapToParentResponse(ParentProfile parent) {
        return ParentProfileResponse.builder()
                .id(parent.getId())
                .userId(parent.getUser() != null ? parent.getUser().getId() : null)
                .fullName(parent.getFullName())
                .phone(parent.getPhone())
                .occupation(parent.getOccupation())
                .avatarUrl(parent.getAvatarUrl())
                .build();
    }

    private ParentChildrenResponse.ChildSummary mapToChildSummary(ParentChild pc) {
        ChildProfile child = pc.getChild();
        return ParentChildrenResponse.ChildSummary.builder()
                .id(child.getId())
                .childUserId(child.getUser() != null ? child.getUser().getId() : null)
                .nickname(child.getNickname())
                .avatarUrl(child.getAvatarUrl())
                .level(child.getLevel())
                .xp(child.getXp())
                .relationship(pc.getRelationship())
                .build();
    }
}
