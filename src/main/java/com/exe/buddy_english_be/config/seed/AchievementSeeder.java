package com.exe.buddy_english_be.config.seed;

import java.util.List;

import org.springframework.stereotype.Component;

import com.exe.buddy_english_be.modules.achievement.entity.Achievement;
import com.exe.buddy_english_be.modules.achievement.repository.AchievementRepository;
import com.exe.buddy_english_be.modules.reward.enums.RewardType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AchievementSeeder {

    private final AchievementRepository achievementRepository;

    public void seed() {
        achievementRepository.saveAll(List.of(
                Achievement.builder().name("First Steps").description("Complete your first speaking scenario").conditionType("SCENARIO_COMPLETE").conditionValue(1).rewardCoin(20).rewardXp(50).rewardType(RewardType.COIN).rewardValue("20").icon("first_steps.png").build(),
                Achievement.builder().name("Animal Master").description("Learn 5 animal vocabulary words").conditionType("VOCAB_ANIMAL").conditionValue(5).rewardCoin(50).rewardXp(100).rewardType(RewardType.TITLE).rewardValue("ANIMAL_MASTER").icon("animal_master.png").build()
        ));
    }
}
