package com.exe.buddy_english_be.config.seed.world;

import java.util.List;

import org.springframework.stereotype.Component;

import com.exe.buddy_english_be.modules.learning.entity.Scenario;
import com.exe.buddy_english_be.modules.learning.entity.ScenarioStep;
import com.exe.buddy_english_be.modules.learning.entity.ScenarioVocabulary;
import com.exe.buddy_english_be.modules.learning.entity.World;
import com.exe.buddy_english_be.modules.learning.repository.ScenarioRepository;
import com.exe.buddy_english_be.modules.learning.repository.ScenarioStepRepository;
import com.exe.buddy_english_be.modules.learning.repository.ScenarioVocabularyRepository;
import com.exe.buddy_english_be.modules.learning.repository.WorldRepository;
import com.exe.buddy_english_be.modules.vocabulary.entity.Vocabulary;
import com.exe.buddy_english_be.modules.vocabulary.entity.VocabularyCategory;
import com.exe.buddy_english_be.modules.vocabulary.repository.VocabularyRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FoodForestSeeder {

    private final WorldRepository worldRepository;
    private final ScenarioRepository scenarioRepository;
    private final VocabularyRepository vocabularyRepository;
    private final ScenarioVocabularyRepository scenarioVocabularyRepository;
    private final ScenarioStepRepository scenarioStepRepository;

    public void seed(VocabularyCategory foodCategory) {
        World foodForest = worldRepository.save(World.builder()
                .name("Food Forest")
                .description("Help Buddy prepare delicious breakfasts while learning English food vocabulary.")
                .thumbnail("/assets/worlds/food-forest.png")
                .orderIndex(1)
                .isActive(true)
                .build());

        Scenario breakfastTrouble = scenarioRepository.save(Scenario.builder()
                .world(foodForest)
                .title("Breakfast Trouble")
                .description("Buddy is hungry. Prepare breakfast for Buddy.")
                .scenarioType("DRAG_DROP")
                .expectedIntent("MAKE_BREAKFAST")
                .difficulty("EASY")
                .orderIndex(1)
                .build());

        Vocabulary apple = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Apple")
                .phonetic("/ˈæp.əl/")
                .meaning("Quả táo")
                .exampleSentence("Buddy eats an apple.")
                .imageUrl("/images/vocabulary/apple.png")
                .difficulty("EASY")
                .build());
        Vocabulary orange = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Orange")
                .phonetic("/ˈɒr.ɪndʒ/")
                .meaning("Quả cam")
                .exampleSentence("Buddy likes oranges.")
                .imageUrl("/images/vocabulary/oranges.png")
                .difficulty("EASY")
                .build());
        Vocabulary pear = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Pear")
                .phonetic("/peər/")
                .meaning("Quả lê")
                .exampleSentence("This pear is sweet.")
                .imageUrl("/images/vocabulary/pear.png")
                .difficulty("EASY")
                .build());
        Vocabulary grapes = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Grapes")
                .phonetic("/ɡreɪps/")
                .meaning("Nho")
                .exampleSentence("I like eating grapes.")
                .imageUrl("/images/vocabulary/grapes.png")
                .difficulty("EASY")
                .build());
        Vocabulary milk = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Milk")
                .phonetic("/mɪlk/")
                .meaning("Sữa")
                .exampleSentence("Buddy drinks milk.")
                .imageUrl("/images/vocabulary/milk.png")
                .difficulty("EASY")
                .build());
        Vocabulary bread = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Bread")
                .phonetic("/bred/")
                .meaning("Bánh mì")
                .exampleSentence("This is bread.")
                .imageUrl("/images/vocabulary/bread.png")
                .difficulty("EASY")
                .build());
        Vocabulary egg = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Egg")
                .phonetic("/eɡ/")
                .meaning("Trứng")
                .exampleSentence("I eat an egg.")
                .imageUrl("/images/vocabulary/egg.png")
                .difficulty("EASY")
                .build());
        Vocabulary eggOnToast = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Egg on Toast")
                .phonetic("/eɡ ɒn təʊst/")
                .meaning("Bánh mì nướng với trứng")
                .exampleSentence("Buddy is eating egg on toast.")
                .imageUrl("/images/vocabulary/egg-on-toast.png")
                .difficulty("MEDIUM")
                .build());

        scenarioVocabularyRepository.saveAll(List.of(
                ScenarioVocabulary.builder().scenario(breakfastTrouble).vocabulary(bread).build(),
                ScenarioVocabulary.builder().scenario(breakfastTrouble).vocabulary(egg).build(),
                ScenarioVocabulary.builder().scenario(breakfastTrouble).vocabulary(milk).build(),
                ScenarioVocabulary.builder().scenario(breakfastTrouble).vocabulary(apple).build(),
                ScenarioVocabulary.builder().scenario(breakfastTrouble).vocabulary(orange).build(),
                ScenarioVocabulary.builder().scenario(breakfastTrouble).vocabulary(pear).build(),
                ScenarioVocabulary.builder().scenario(breakfastTrouble).vocabulary(grapes).build(),
                ScenarioVocabulary.builder().scenario(breakfastTrouble).vocabulary(eggOnToast).build()
        ));

        scenarioStepRepository.saveAll(List.of(
                ScenarioStep.builder().scenario(breakfastTrouble).stepOrder(1).buddyMessage("I'm really hungry! Can you make Egg on Toast for me?").expectedIntent("MAKE_FOOD").expectedEntity("EGG_ON_TOAST").successResponse("Wow! Egg on Toast looks delicious!").failResponse("Hmm... I want Egg on Toast. Try putting the egg and toast into the pot!").build(),
                ScenarioStep.builder().scenario(breakfastTrouble).stepOrder(2).buddyMessage("Thank you! I'm still thirsty. Can I have some milk?").expectedIntent("GIVE_ITEM").expectedEntity("MILK").successResponse("Yummy! Milk is my favorite drink. Not only does it taste good, but it's also good for my bones!").failResponse("Oops! That's not milk.").build(),
                ScenarioStep.builder().scenario(breakfastTrouble).stepOrder(3).buddyMessage("I'm almost full! Can I have an apple too?").expectedIntent("GIVE_ITEM").expectedEntity("APPLE").successResponse("Crunch! Apples are healthy and delicious!").failResponse("I asked for an apple.").build(),
                ScenarioStep.builder().scenario(breakfastTrouble).stepOrder(4).buddyMessage("I'm full now! Thank you for helping me!").expectedIntent("MISSION_COMPLETE").expectedEntity(null).successResponse("Mission Complete! Great job!").failResponse(null).build()
        ));
    }
}
