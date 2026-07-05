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
                                ScenarioVocabulary.builder().scenario(breakfastTrouble).vocabulary(eggOnToast)
                                                .build()));

                scenarioStepRepository.saveAll(List.of(
                                ScenarioStep.builder().scenario(breakfastTrouble).stepOrder(1)
                                                .buddyMessage("I'm really hungry! Can you make Egg on Toast for me?")
                                                .expectedIntent("MAKE_FOOD").expectedEntity("EGG_ON_TOAST")
                                                .successResponse("Wow! Egg on Toast looks delicious!")
                                                .failResponse("Hmm... I want Egg on Toast. Try putting the egg and toast into the pot!")
                                                .build(),
                                ScenarioStep.builder().scenario(breakfastTrouble).stepOrder(2)
                                                .buddyMessage("Thank you! I'm still thirsty. Can I have some milk?")
                                                .expectedIntent("GIVE_ITEM").expectedEntity("MILK")
                                                .successResponse(
                                                                "Yummy! Milk is my favorite drink. Not only does it taste good, but it's also good for my bones!")
                                                .failResponse("Oops! That's not milk.").build(),
                                ScenarioStep.builder().scenario(breakfastTrouble).stepOrder(3)
                                                .buddyMessage("I'm almost full! Can I have an apple too?")
                                                .expectedIntent("GIVE_ITEM").expectedEntity("APPLE")
                                                .successResponse("Crunch! Apples are healthy and delicious!")
                                                .failResponse("I asked for an apple.").build(),
                                ScenarioStep.builder().scenario(breakfastTrouble).stepOrder(4)
                                                .buddyMessage("I'm full now! Thank you for helping me!")
                                                .expectedIntent("MISSION_COMPLETE").expectedEntity(null)
                                                .successResponse("Mission Complete! Great job!").failResponse(null)
                                                .build()));

                Scenario supermarketShopping = scenarioRepository.save(Scenario.builder()
                                .world(foodForest)
                                .title("Supermarket Shopping")
                                .description("Help Buddy buy all the ingredients for dinner.")
                                .scenarioType("SHOPPING")
                                .expectedIntent("SHOPPING")
                                .difficulty("MEDIUM")
                                .orderIndex(2)
                                .build());

                Vocabulary broccoli = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Broccoli")
                                .phonetic("/ˈbrɒk.əl.i/")
                                .meaning("Bông cải xanh")
                                .exampleSentence("This broccoli is fresh.")
                                .imageUrl("/images/vocabulary/broccoli.png")
                                .difficulty("EASY")
                                .build());

                Vocabulary cucumber = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Cucumber")
                                .phonetic("/ˈkjuː.kʌm.bər/")
                                .meaning("Dưa chuột")
                                .exampleSentence("I like cucumber salad.")
                                .imageUrl("/images/vocabulary/cucumber.png")
                                .difficulty("EASY")
                                .build());

                Vocabulary potato = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Potato")
                                .phonetic("/pəˈteɪ.təʊ/")
                                .meaning("Khoai tây")
                                .exampleSentence("I want to buy potatoes.")
                                .imageUrl("/images/vocabulary/potato.png")
                                .difficulty("EASY")
                                .build());

                Vocabulary onion = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Onion")
                                .phonetic("/ˈʌn.jən/")
                                .meaning("Hành tây")
                                .exampleSentence("An onion makes me cry.")
                                .imageUrl("/images/vocabulary/onion.png")
                                .difficulty("EASY")
                                .build());

                Vocabulary cheese = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Cheese")
                                .phonetic("/tʃiːz/")
                                .meaning("Phô mai")
                                .exampleSentence("Cheese is my favorite food.")
                                .imageUrl("/images/vocabulary/cheese.png")
                                .difficulty("EASY")
                                .build());

                Vocabulary yogurt = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Yogurt")
                                .phonetic("/ˈjəʊ.ɡət/")
                                .meaning("Sữa chua")
                                .exampleSentence("Yogurt is good for your health.")
                                .imageUrl("/images/vocabulary/yogurt.png")
                                .difficulty("EASY")
                                .build());

                Vocabulary butter = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Butter")
                                .phonetic("/ˈbʌt.ər/")
                                .meaning("Bơ")
                                .exampleSentence("Butter is good for your health.")
                                .imageUrl("/images/vocabulary/butter.png")
                                .difficulty("EASY")
                                .build());

                Vocabulary chicken = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Chicken")
                                .phonetic("/ˈtʃɪk.ɪn/")
                                .meaning("Thịt gà")
                                .exampleSentence("I enjoy eating fried chicken.")
                                .imageUrl("/images/vocabulary/chicken.png")
                                .difficulty("EASY")
                                .build());

                Vocabulary beef = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Beef")
                                .phonetic("/biːf/")
                                .meaning("Thịt bò")
                                .exampleSentence("Beef steak is my favorite.")
                                .imageUrl("/images/vocabulary/beef.png")
                                .difficulty("EASY")
                                .build());

                Vocabulary pork = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Pork")
                                .phonetic("/pɔːk/")
                                .meaning("Thịt heo")
                                .exampleSentence("We have pork and beef for dinner.")
                                .imageUrl("/images/vocabulary/pork.png")
                                .difficulty("EASY")
                                .build());

                // distractor
                Vocabulary banana = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Banana")
                                .phonetic("/bəˈnɑː.nə/")
                                .meaning("Chuối")
                                .exampleSentence("I like bananas.")
                                .imageUrl("/images/vocabulary/banana.png")
                                .difficulty("EASY")
                                .build());

                Vocabulary watermelon = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Watermelon")
                                .phonetic("/ˈwɔː.təˌmel.ən/")
                                .meaning("Dưa hấu")
                                .exampleSentence("I like watermelon.")
                                .imageUrl("/images/vocabulary/watermelon.png")
                                .difficulty("EASY")
                                .build());

                Vocabulary chocolate = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Chocolate")
                                .phonetic("/ˈtʃɒk.lət/")
                                .meaning("Sô cô la")
                                .exampleSentence("I like chocolate.")
                                .imageUrl("/images/vocabulary/chocolate.png")
                                .difficulty("EASY")
                                .build());

                Vocabulary candy = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Candy")
                                .phonetic("/ˈkæn.di/")
                                .meaning("Kẹo")
                                .exampleSentence("I like candy.")
                                .imageUrl("/images/vocabulary/candy.png")
                                .difficulty("EASY")
                                .build());

                Vocabulary fish = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Fish")
                                .phonetic("/fɪʃ/")
                                .meaning("Cá")
                                .exampleSentence("I like fish.")
                                .imageUrl("/images/vocabulary/fish.png")
                                .difficulty("EASY")
                                .build());

                Vocabulary shrimp = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Shrimp")
                                .phonetic("/ʃrɪmp/")
                                .meaning("Tôm")
                                .exampleSentence("I like shrimp.")
                                .imageUrl("/images/vocabulary/shrimp.png")
                                .difficulty("EASY")
                                .build());

                Vocabulary crab = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Crab")
                                .phonetic("/kræb/")
                                .meaning("Cua")
                                .exampleSentence("I like crab.")
                                .imageUrl("/images/vocabulary/crab.png")
                                .difficulty("EASY")
                                .build());

                Vocabulary crackers = vocabularyRepository.save(Vocabulary.builder()
                                .category(foodCategory)
                                .word("Crackers")
                                .phonetic("/ˈkræk.əz/")
                                .meaning("Bánh quy mặn")
                                .exampleSentence("I like crackers.")
                                .imageUrl("/images/vocabulary/crackers.png")
                                .difficulty("EASY")
                                .build());
                scenarioVocabularyRepository.saveAll(List.of(

                                // checklist

                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(broccoli).build(),
                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(cucumber).build(),
                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(potato).build(),
                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(onion).build(),

                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(cheese).build(),
                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(yogurt).build(),
                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(butter).build(),
                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(crackers).build(),

                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(chicken).build(),
                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(beef).build(),
                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(pork).build(),

                                // distractor

                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(banana).build(),
                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(watermelon)
                                                .build(),
                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(chocolate)
                                                .build(),
                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(candy).build(),
                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(fish).build(),
                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(shrimp).build(),
                                ScenarioVocabulary.builder().scenario(supermarketShopping).vocabulary(crab).build()

                ));
                scenarioStepRepository.saveAll(List.of(

                                // Step 1

                                ScenarioStep.builder()
                                                .scenario(supermarketShopping)
                                                .stepOrder(1)
                                                .buddyMessage(
                                                                "Let's buy some vegetables first! Please collect broccoli, cucumber, potato, and onion.")
                                                .expectedIntent("COLLECT_CATEGORY")
                                                .expectedEntity("VEGETABLES")
                                                .successResponse("Awesome! We have all the vegetables.")
                                                .failResponse("We still need broccoli, cucumber, potato, and onion.")
                                                .build(),

                                // Step 2

                                ScenarioStep.builder()
                                                .scenario(supermarketShopping)
                                                .stepOrder(2)
                                                .buddyMessage(
                                                                "Great! Now let's get some dairy and snacks. We need cheese, yogurt and butter.")
                                                .expectedIntent("COLLECT_CATEGORY")
                                                .expectedEntity("SNACK_DAIRY")
                                                .successResponse("Perfect! Everything from the fridge is ready.")
                                                .failResponse("Check the fridge again.")
                                                .build(),

                                // Step 3

                                ScenarioStep.builder()
                                                .scenario(supermarketShopping)
                                                .stepOrder(3)
                                                .buddyMessage(
                                                                "Hmm... I don't know where the meat section is. Can you ask the staff where the meat counter is?")
                                                .expectedIntent("ASK_LOCATION")
                                                .expectedEntity("MEAT_COUNTER")
                                                .successResponse("Great! Now we know where to go.")
                                                .failResponse("Try asking: 'Excuse me, where is the meat counter?'")
                                                .build(),

                                // Step 4

                                ScenarioStep.builder()
                                                .scenario(supermarketShopping)
                                                .stepOrder(4)
                                                .buddyMessage(
                                                                "Let's buy chicken, beef, and pork.")
                                                .expectedIntent("COLLECT_CATEGORY")
                                                .expectedEntity("MEAT")
                                                .successResponse("Excellent! We bought all the meat we need.")
                                                .failResponse("Don't forget: chicken, beef, and pork.")
                                                .build(),

                                // Step 5

                                ScenarioStep.builder()
                                                .scenario(supermarketShopping)
                                                .stepOrder(5)
                                                .buddyMessage(
                                                                "Everything is in our basket! Let's go to the checkout.")
                                                .expectedIntent("MISSION_COMPLETE")
                                                .expectedEntity(null)
                                                .successResponse("Mission Complete! You're a great shopper!")
                                                .failResponse(null)
                                                .build()

                ));
        }
}
