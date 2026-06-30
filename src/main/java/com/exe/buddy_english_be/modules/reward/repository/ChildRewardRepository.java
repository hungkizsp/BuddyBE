package com.exe.buddy_english_be.modules.reward.repository;

import com.exe.buddy_english_be.modules.reward.entity.ChildReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildRewardRepository extends JpaRepository<ChildReward, Long> {

    List<ChildReward> findByChildId(Long childId);

    List<ChildReward> findByChildIdAndEquippedTrue(Long childId);
}
