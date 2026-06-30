package com.exe.buddy_english_be.modules.progress.repository;

import com.exe.buddy_english_be.modules.progress.entity.ChildAdventureProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChildAdventureProgressRepository extends JpaRepository<ChildAdventureProgress, Long> {

    List<ChildAdventureProgress> findByChildId(Long childId);

    Optional<ChildAdventureProgress> findByChildIdAndAdventureId(Long childId, Long adventureId);
}
