package com.exe.buddy_english_be.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.exe.buddy_english_be.config.seed.AchievementSeeder;
import com.exe.buddy_english_be.config.seed.MissionSeeder;
import com.exe.buddy_english_be.config.seed.RewardSeeder;
import com.exe.buddy_english_be.config.seed.RoleSeeder;
import com.exe.buddy_english_be.config.seed.UserSeeder;
import com.exe.buddy_english_be.config.seed.VocabularySeeder;
import com.exe.buddy_english_be.config.seed.world.FoodForestSeeder;
import com.exe.buddy_english_be.modules.learning.repository.WorldRepository;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.user.repository.UserRepository;
import com.exe.buddy_english_be.modules.vocabulary.entity.VocabularyCategory;
import com.exe.buddy_english_be.modules.vocabulary.repository.VocabularyCategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Seeds test data on startup for the restructured 37-table BuddyEnglish system.
 * Only inserts data when the test user "testuser@buddy.com" does not exist.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleSeeder roleSeeder;
    private final UserSeeder userSeeder;
    private final VocabularySeeder vocabularySeeder;
    private final MissionSeeder missionSeeder;
    private final RewardSeeder rewardSeeder;
    private final AchievementSeeder achievementSeeder;
    private final WorldRepository worldRepository;
    private final VocabularyCategoryRepository categoryRepository;
    private final FoodForestSeeder foodForestSeeder;

    @Override
    public void run(String... args) {
        ChildProfile childProfile = null;
        if (userRepository.findByEmail("testuser@buddy.com").isEmpty()) {
            log.info("Seeding test user & initial data...");
            roleSeeder.seed();
            childProfile = userSeeder.seed();
            vocabularySeeder.seed(childProfile);
            missionSeeder.seed();
            rewardSeeder.seed();
            achievementSeeder.seed();
        } else {
            log.info("Test user already exists — checking other seeders.");
        }

        if (worldRepository.count() == 0) {
            log.info("Seeding World & Scenario data (FoodForestSeeder)...");
            VocabularyCategory foodCat = categoryRepository.findByName("Food")
                    .orElseGet(() -> categoryRepository.save(VocabularyCategory.builder().name("Food").build()));
            foodForestSeeder.seed(foodCat);
            log.info("World & Scenario data seeding complete.");
        }
    }
}
