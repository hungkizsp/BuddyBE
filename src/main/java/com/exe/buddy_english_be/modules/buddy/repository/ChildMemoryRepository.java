package com.exe.buddy_english_be.modules.buddy.repository;

import com.exe.buddy_english_be.modules.buddy.entity.ChildMemory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChildMemoryRepository extends JpaRepository<ChildMemory, Long> {

    List<ChildMemory> findByChildIdAndIsActiveTrueOrderByImportanceDesc(Long childId);

    Optional<ChildMemory> findByChildIdAndMemoryKeyAndIsActiveTrue(Long childId, String memoryKey);

    List<ChildMemory> findByChildIdAndMemoryTypeAndIsActiveTrue(Long childId, String memoryType);
}
