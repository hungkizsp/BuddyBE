package com.exe.buddy_english_be.modules.buddy.repository;

import com.exe.buddy_english_be.modules.buddy.entity.BuddyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuddyProfileRepository extends JpaRepository<BuddyProfile, Long> {

    Optional<BuddyProfile> findByChildId(Long childId);
}
