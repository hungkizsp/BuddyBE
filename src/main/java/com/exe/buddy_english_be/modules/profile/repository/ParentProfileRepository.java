package com.exe.buddy_english_be.modules.profile.repository;

import com.exe.buddy_english_be.modules.profile.entity.ParentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentProfileRepository extends JpaRepository<ParentProfile, Long> {

    Optional<ParentProfile> findByUserId(Long userId);
}
