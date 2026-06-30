package com.exe.buddy_english_be.modules.profile.repository;

import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChildProfileRepository extends JpaRepository<ChildProfile, Long> {

    Optional<ChildProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
