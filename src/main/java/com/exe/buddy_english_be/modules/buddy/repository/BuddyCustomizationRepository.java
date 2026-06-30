package com.exe.buddy_english_be.modules.buddy.repository;

import com.exe.buddy_english_be.modules.buddy.entity.BuddyCustomization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuddyCustomizationRepository extends JpaRepository<BuddyCustomization, Long> {
}
