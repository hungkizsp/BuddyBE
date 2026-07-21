package com.exe.buddy_english_be.modules.character.repository;

import com.exe.buddy_english_be.modules.character.entity.CustomCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomCharacterRepository extends JpaRepository<CustomCharacter, Long> {

    List<CustomCharacter> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<CustomCharacter> findByTaskId(String taskId);

    Optional<CustomCharacter> findByIdAndUserId(Long id, Long userId);
}
