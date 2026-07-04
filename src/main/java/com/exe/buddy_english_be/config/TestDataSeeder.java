package com.exe.buddy_english_be.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.exe.buddy_english_be.modules.achievement.entity.Achievement;
import com.exe.buddy_english_be.modules.achievement.repository.AchievementRepository;
import com.exe.buddy_english_be.modules.buddy.entity.BuddyProfile;
import com.exe.buddy_english_be.modules.buddy.enums.BuddyMood;
import com.exe.buddy_english_be.modules.buddy.repository.BuddyProfileRepository;
import com.exe.buddy_english_be.modules.learning.entity.Scenario;
import com.exe.buddy_english_be.modules.learning.entity.ScenarioVocabulary;
import com.exe.buddy_english_be.modules.learning.entity.World;
import com.exe.buddy_english_be.modules.learning.repository.ScenarioRepository;
import com.exe.buddy_english_be.modules.learning.repository.ScenarioVocabularyRepository;
import com.exe.buddy_english_be.modules.learning.repository.WorldRepository;
import com.exe.buddy_english_be.modules.mission.entity.Mission;
import com.exe.buddy_english_be.modules.mission.enums.MissionFrequency;
import com.exe.buddy_english_be.modules.mission.enums.TargetType;
import com.exe.buddy_english_be.modules.mission.repository.MissionRepository;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import com.exe.buddy_english_be.modules.progress.entity.ChildVocabularyProgress;
import com.exe.buddy_english_be.modules.progress.repository.ChildVocabularyProgressRepository;
import com.exe.buddy_english_be.modules.reward.entity.Reward;
import com.exe.buddy_english_be.modules.reward.enums.RewardRarity;
import com.exe.buddy_english_be.modules.reward.enums.RewardType;
import com.exe.buddy_english_be.modules.reward.repository.RewardRepository;
import com.exe.buddy_english_be.modules.user.entity.Role;
import com.exe.buddy_english_be.modules.user.entity.User;
import com.exe.buddy_english_be.modules.user.repository.RoleRepository;
import com.exe.buddy_english_be.modules.user.repository.UserRepository;
import com.exe.buddy_english_be.modules.vocabulary.entity.Vocabulary;
import com.exe.buddy_english_be.modules.vocabulary.entity.VocabularyCategory;
import com.exe.buddy_english_be.modules.vocabulary.repository.VocabularyCategoryRepository;
import com.exe.buddy_english_be.modules.vocabulary.repository.VocabularyRepository;

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
    private final RoleRepository roleRepository;
    private final ChildProfileRepository childProfileRepository;
    private final BuddyProfileRepository buddyProfileRepository;
    private final VocabularyCategoryRepository categoryRepository;
    private final VocabularyRepository vocabularyRepository;
    private final ChildVocabularyProgressRepository vocabularyProgressRepository;
    private final MissionRepository missionRepository;
    private final RewardRepository rewardRepository;
    private final AchievementRepository achievementRepository;
    private final PasswordEncoder passwordEncoder;
    private final WorldRepository worldRepository;
    private final ScenarioRepository scenarioRepository;
    private final ScenarioVocabularyRepository scenarioVocabularyRepository;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("testuser@buddy.com").isPresent()) {
            log.info("Test user already exists — skipping test data seeding.");
            return;
        }

        log.info("Seeding test data for restructured schema...");

        // 1. Seed Roles
        Role adminRole = roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
        Role parentRole = roleRepository.save(Role.builder().name("ROLE_PARENT").build());
        Role childRole = roleRepository.save(Role.builder().name("ROLE_CHILD").build());
        Role teacherRole = roleRepository.save(Role.builder().name("ROLE_TEACHER").build());

        // 2. Create test user (Child)
        User user = userRepository.save(User.builder()
                .email("testuser@buddy.com")
                .passwordHash(passwordEncoder.encode("password123"))
                .roles(Set.of(childRole))
                .build());

        // 3. Create Child Profile
        ChildProfile childProfile = childProfileRepository.save(ChildProfile.builder()
                .user(user)
                .nickname("testuser")
                .level(2)
                .xp(150)
                .coins(50)
                .streakDays(2)
                .lastLoginDate(LocalDate.now())
                .build());

        // 4. Create Buddy Profile
        buddyProfileRepository.save(BuddyProfile.builder()
                .child(childProfile)
                .name("Buddy")
                .level(2)
                .friendshipPoints(20)
                .mood(BuddyMood.HAPPY)
                .energy(90)
                .lastInteractionAt(LocalDateTime.now())
                .build());

        // 5. Seed Vocabulary Categories
        VocabularyCategory foodCat = categoryRepository.save(VocabularyCategory.builder().name("Food").build());
        VocabularyCategory animalCat = categoryRepository.save(VocabularyCategory.builder().name("Animals").build());
        VocabularyCategory emotionCat = categoryRepository.save(VocabularyCategory.builder().name("Emotions").build());
        VocabularyCategory placeCat = categoryRepository.save(VocabularyCategory.builder().name("Places").build());

        // 6. Seed Vocabulary Words
        List<Vocabulary> vocabs = vocabularyRepository.saveAll(List.of(
                Vocabulary.builder().word("apple").meaning("a round fruit with red or green skin").category(foodCat).difficulty("EASY").build(),
                Vocabulary.builder().word("banana").meaning("a long curved yellow fruit").category(foodCat).difficulty("EASY").build(),
                Vocabulary.builder().word("water").meaning("a clear liquid that you drink").category(foodCat).difficulty("EASY").build(),
                Vocabulary.builder().word("dog").meaning("a common pet animal that barks").category(animalCat).difficulty("EASY").build(),
                Vocabulary.builder().word("cat").meaning("a small pet animal that meows").category(animalCat).difficulty("EASY").build(),
                Vocabulary.builder().word("bird").meaning("an animal with feathers and wings that can fly").category(animalCat).difficulty("EASY").build(),
                Vocabulary.builder().word("happy").meaning("feeling pleased and joyful").category(emotionCat).difficulty("EASY").build(),
                Vocabulary.builder().word("sad").meaning("feeling unhappy or sorrowful").category(emotionCat).difficulty("EASY").build(),
                Vocabulary.builder().word("school").meaning("a place where children go to learn").category(placeCat).difficulty("MEDIUM").build(),
                Vocabulary.builder().word("hospital").meaning("a place where sick people are treated by doctors").category(placeCat).difficulty("MEDIUM").build()
        ));

        // 7. Seed Child Vocabulary Progress (Spaced Repetition Practice)
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < vocabs.size(); i++) {
            vocabularyProgressRepository.save(ChildVocabularyProgress.builder()
                    .child(childProfile)
                    .vocabulary(vocabs.get(i))
                    .masteryLevel(1)
                    .correctCount(1)
                    .wrongCount(0)
                    .confidenceScore(1.0)
                    .firstLearnedAt(now.minusDays(1))
                    .lastPracticed(now.minusHours(2))
                    .nextReviewAt(now.plusDays(i % 2 == 0 ? 1 : 2))
                    .build());
        }

        // 8. Seed Gamification Missions
        missionRepository.saveAll(List.of(
                Mission.builder().title("Daily Talker").description("Speak 5 sentences to Buddy").missionFrequency(MissionFrequency.DAILY).targetType(TargetType.SPEAK_TIMES).targetValue(5).rewardCoin(10).rewardXp(15).build(),
                Mission.builder().title("Vocab Starter").description("Learn 3 new vocabulary words").missionFrequency(MissionFrequency.DAILY).targetType(TargetType.COMPLETE_ADVENTURE).targetValue(3).rewardCoin(15).rewardXp(20).build(),
                Mission.builder().title("Weekly Champion").description("Maintain a 7-day streak").missionFrequency(MissionFrequency.WEEKLY).targetType(TargetType.LOGIN_DAYS).targetValue(7).rewardCoin(50).rewardXp(100).build()
        ));

        // 9. Seed Rewards (Shop customizations)
        rewardRepository.saveAll(List.of(
                Reward.builder().name("Pirate Hat").type(RewardType.ITEM).price(30).rarity(RewardRarity.RARE).isLimited(false).imageUrl("pirate_hat.png").build(),
                Reward.builder().name("Red Cape").type(RewardType.ITEM).price(20).rarity(RewardRarity.COMMON).isLimited(false).imageUrl("red_cape.png").build(),
                Reward.builder().name("Golden Crown").type(RewardType.ITEM).price(100).rarity(RewardRarity.LEGENDARY).isLimited(true).availableFrom(now.minusDays(1)).availableTo(now.plusDays(30)).imageUrl("golden_crown.png").build()
        ));

        // 10. Seed Achievements
        achievementRepository.saveAll(List.of(
                Achievement.builder().name("First Steps").description("Complete your first speaking scenario").conditionType("SCENARIO_COMPLETE").conditionValue(1).rewardCoin(20).rewardXp(50).rewardType(RewardType.COIN).rewardValue("20").icon("first_steps.png").build(),
                Achievement.builder().name("Animal Master").description("Learn 5 animal vocabulary words").conditionType("VOCAB_ANIMAL").conditionValue(5).rewardCoin(50).rewardXp(100).rewardType(RewardType.TITLE).rewardValue("ANIMAL_MASTER").icon("animal_master.png").build()
        ));
        
        World foodForest = worldRepository.save(
    World.builder()
            .name("Food Forest")
            .description("Help Buddy prepare delicious breakfasts while learning English food vocabulary.")
            .thumbnail("/assets/worlds/food-forest.png")
            .orderIndex(1)
            .isActive(true)
            .build()
);
Scenario breakfastTrouble = scenarioRepository.save(
        Scenario.builder()
                .world(foodForest)
                .title("Breakfast Trouble")
                .description("Buddy is hungry. Prepare breakfast for Buddy.")
                .scenarioType("DRAG_DROP")
                .expectedIntent("MAKE_BREAKFAST")
                .difficulty("EASY")
                .orderIndex(1)
                .build()
);
VocabularyCategory foodCategory = foodCat;
        Vocabulary apple = vocabularyRepository.save(
        Vocabulary.builder()
                .category(foodCategory)
                .word("Apple")
                .phonetic("/ˈæp.əl/")
                .meaning("Quả táo")
                .exampleSentence("Buddy eats an apple.")
                .imageUrl("/images/vocabulary/apple.png")
                .difficulty("EASY")
                .build()
);

Vocabulary milk = vocabularyRepository.save(
        Vocabulary.builder()
                .category(foodCategory)
                .word("Milk")
                .phonetic("/mɪlk/")
                .meaning("Sữa")
                .exampleSentence("Buddy drinks milk.")
                .imageUrl("/images/vocabulary/milk.png")
                .difficulty("EASY")
                .build()
);

Vocabulary bread = vocabularyRepository.save(
        Vocabulary.builder()
                .category(foodCategory)
                .word("Bread")
                .phonetic("/bred/")
                .meaning("Bánh mì")
                .exampleSentence("This is bread.")
                .imageUrl("/images/vocabulary/bread.png")
                .difficulty("EASY")
                .build()
);

Vocabulary egg = vocabularyRepository.save(
        Vocabulary.builder()
                .category(foodCategory)
                .word("Egg")
                .phonetic("/eɡ/")
                .meaning("Trứng")
                .exampleSentence("I eat an egg.")
                .imageUrl("/images/vocabulary/egg.png")
                .difficulty("EASY")
                .build()
);

Vocabulary toast = vocabularyRepository.save(
        Vocabulary.builder()
                .category(foodCategory)
                .word("Toast")
                .phonetic("/təʊst/")
                .meaning("Bánh mì nướng")
                .exampleSentence("Buddy likes toast.")
                .imageUrl("/images/vocabulary/toast.png")
                .difficulty("EASY")
                .build()
);

Vocabulary eggOnToast = vocabularyRepository.save(
        Vocabulary.builder()
                .category(foodCategory)
                .word("Egg on Toast")
                .phonetic("/eɡ ɒn təʊst/")
                .meaning("Bánh mì nướng với trứng")
                .exampleSentence("Buddy is eating egg on toast.")
                .imageUrl("/images/vocabulary/egg_on_toast.png")
                .difficulty("MEDIUM")
                .build()
);
scenarioVocabularyRepository.saveAll(List.of(

        ScenarioVocabulary.builder()
                .scenario(breakfastTrouble)
                .vocabulary(bread)
                .build(),

        ScenarioVocabulary.builder()
                .scenario(breakfastTrouble)
                .vocabulary(egg)
                .build(),

        ScenarioVocabulary.builder()
                .scenario(breakfastTrouble)
                .vocabulary(toast)
                .build(),

        ScenarioVocabulary.builder()
                .scenario(breakfastTrouble)
                .vocabulary(milk)
                .build(),

        ScenarioVocabulary.builder()
                .scenario(breakfastTrouble)
                .vocabulary(apple)
                .build(),

        ScenarioVocabulary.builder()
                .scenario(breakfastTrouble)
                .vocabulary(eggOnToast)
                .build()

));
        log.info("Test data seeded: 4 roles, 1 user, 1 child profile, 1 buddy profile, 10 vocabularies linked to child, 3 missions, 3 shop rewards, 2 achievements.");
    }
}
