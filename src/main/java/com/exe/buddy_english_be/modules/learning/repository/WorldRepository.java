package com.exe.buddy_english_be.modules.learning.repository;

import com.exe.buddy_english_be.modules.learning.entity.World;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorldRepository extends JpaRepository<World, Long> {

    List<World> findByIsActiveTrueOrderByOrderIndexAsc();
}
