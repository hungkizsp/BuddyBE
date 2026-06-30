package com.exe.buddy_english_be.modules.buddy.repository;

import com.exe.buddy_english_be.modules.buddy.entity.ChildCustomization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildCustomizationRepository extends JpaRepository<ChildCustomization, Long> {

    List<ChildCustomization> findByChildId(Long childId);

    List<ChildCustomization> findByChildIdAndEquippedTrue(Long childId);
}
