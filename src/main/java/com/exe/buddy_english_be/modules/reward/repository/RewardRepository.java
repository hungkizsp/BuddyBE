package com.exe.buddy_english_be.modules.reward.repository;

import com.exe.buddy_english_be.modules.reward.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
}
