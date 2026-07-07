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
                .description("Giúp Buddy chuẩn bị bữa sáng thật ngon và học từ vựng tiếng Anh về đồ ăn nhé!")
                .thumbnail("/assets/worlds/food-forest.png")
                .orderIndex(1)
                .isActive(true)
                .build());

        Scenario breakfastTrouble = scenarioRepository.save(Scenario.builder()
                .world(foodForest)
                .title("Breakfast Trouble")
                .description("Buddy đói bụng quá! Hãy chuẩn bị bữa sáng cho Buddy nào.")
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
                        .buddyMessage("Mình đói quá! Bạn làm Egg on Toast cho mình được không? Thử bỏ Egg và Toast vào nồi nhé!")
                        .expectedIntent("MAKE_FOOD").expectedEntity("EGG_ON_TOAST")
                        .successResponse("Chà! Egg on Toast trông ngon quá!")
                        .failResponse("Hmm... Mình muốn Egg on Toast. Thử bỏ egg và toast vào nồi nhé!")
                        .build(),
                ScenarioStep.builder().scenario(breakfastTrouble).stepOrder(2)
                        .buddyMessage("Cảm ơn bạn! Mình vẫn còn khát. Cho mình xin Milk nhé?")
                        .expectedIntent("GIVE_ITEM").expectedEntity("MILK")
                        .successResponse(
                                "Ngon quá! Milk là thức uống yêu thích của mình. Vừa ngon lại vừa tốt cho xương nữa!")
                        .failResponse("Ối! Đó không phải Milk.").build(),
                ScenarioStep.builder().scenario(breakfastTrouble).stepOrder(3)
                        .buddyMessage("Mình gần no rồi! Cho mình xin thêm Apple nhé?")
                        .expectedIntent("GIVE_ITEM").expectedEntity("APPLE")
                        .successResponse("Giòn quá! Apple vừa tốt cho sức khỏe vừa ngon nữa!")
                        .failResponse("Mình đã xin Apple mà.").build(),
                ScenarioStep.builder().scenario(breakfastTrouble).stepOrder(4)
                        .buddyMessage("Mình no rồi! Cảm ơn bạn đã giúp mình nhé!")
                        .expectedIntent("MISSION_COMPLETE").expectedEntity(null)
                        .successResponse("Hoàn thành nhiệm vụ! Giỏi quá!").failResponse(null)
                        .build()));

        Scenario supermarketShopping = scenarioRepository.save(Scenario.builder()
                .world(foodForest)
                .title("Supermarket Shopping")
                .description("Giúp Buddy mua đủ nguyên liệu cho bữa tối nhé!")
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
                                "Cùng mua rau trước nào! Hãy lấy Broccoli, Cucumber, Potato và Onion nhé!")
                        .expectedIntent("COLLECT_CATEGORY")
                        .expectedEntity("VEGETABLES")
                        .successResponse(
                                "Tuyệt vời! Chúng mình có đủ rau rồi. Giờ cần mua đồ ăn vặt và sữa nữa nhé!")
                        .failResponse("Chúng mình vẫn cần Broccoli, Cucumber, Potato và Onion.")
                        .build(),
                // Step 2

                ScenarioStep.builder()
                        .scenario(supermarketShopping)
                        .stepOrder(2)
                        .buddyMessage(
                                "Tuyệt! Giờ mua đồ ăn vặt và sữa nào. Chúng mình cần Cheese, Yogurt và Butter.")
                        .expectedIntent("COLLECT_CATEGORY")
                        .expectedEntity("SNACK_DAIRY")
                        .successResponse(
                                "Hoàn hảo! Đồ trong tủ lạnh đã đủ cả rồi. Tiếp theo chúng mình cần mua thịt nhưng mình không biết ở đâu. Bạn hỏi nhân viên giúp mình nhé?")
                        .failResponse("Kiểm tra tủ lạnh lần nữa và làm theo danh sách nhé!")
                        .build(),
                // Step 3

                ScenarioStep.builder()
                        .scenario(supermarketShopping)
                        .stepOrder(3)
                        .buddyMessage(
                                "Hmm... Mình không biết khu thịt ở đâu. Bạn hỏi nhân viên giúp mình được không?")
                        .expectedIntent("ASK_LOCATION")
                        .expectedEntity("MEAT_COUNTER")
                        .successResponse("Tuyệt! Giờ mình biết đi đâu rồi!")
                        .failResponse("Hãy thử hỏi: 'Excuse me, quầy thịt ở đâu ạ?'")
                        .build(),
                // Step 4

                ScenarioStep.builder()
                        .scenario(supermarketShopping)
                        .stepOrder(4)
                        .buddyMessage(
                                "Ah, thì ra nó nằm ở bên phải. Giờ cùng mua Chicken, Beef và Pork nào.")
                        .expectedIntent("COLLECT_CATEGORY")
                        .expectedEntity("MEAT")
                        .successResponse("Xuất sắc! Chúng mình đã mua đủ thịt rồi!")
                        .failResponse("Đừng quên: Chicken, Beef và Pork nhé!")
                        .build(),
                // Step 5

                ScenarioStep.builder()
                        .scenario(supermarketShopping)
                        .stepOrder(5)
                        .buddyMessage(
                                "Đồ đã đầy giỏ rồi! Cùng ra quầy thanh toán thôi!")
                        .expectedIntent("MISSION_COMPLETE")
                        .expectedEntity(null)
                        .successResponse("Hoàn thành nhiệm vụ! Bạn là người mua sắm giỏi quá!")
                        .failResponse(null)
                        .build()
        ));
        //new scene
        Scenario FamilyRestaurant = scenarioRepository.save(Scenario.builder()
                .world(foodForest)
                .title("Family Restaurant")
                .description("Hãy cùng Buddy thưởng thức bữa ăn ngon lành tại nhà hàng gia đình!")
                .scenarioType("ORDER_FOOD")
                .expectedIntent("ORDER_FOOD")
                .difficulty("MEDIUM")
                .orderIndex(3)
                .build());

        Vocabulary BroccoliSoup = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Broccoli Soup")
                .phonetic("/ˈbrɒk.əl.i suːp/")
                .meaning("Súp bông cải xanh")
                .exampleSentence("Broccoli soup is healthy.")
                .imageUrl("/images/vocabulary/broccoli-soup.png")
                .difficulty("MEDIUM")
                .build());
        Vocabulary MashedPotatoes = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Mashed Potatoes")
                .phonetic("/mæʃt pəˈteɪ.təʊz/")
                .meaning("Khoai tây nghiền")
                .exampleSentence("Mashed potatoes are creamy.")
                .imageUrl("/images/vocabulary/mashed-potatoes.png")
                .difficulty("MEDIUM")
                .build());
        Vocabulary GarlicBread = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Garlic Bread")
                .phonetic("/ˈɡɑː.lɪk bred/")
                .meaning("Bánh mì tỏi")
                .exampleSentence("Garlic bread is tasty.")
                .imageUrl("/images/vocabulary/garlic-bread.png")
                .difficulty("MEDIUM")
                .build());
        //step2
        Vocabulary GrilledChicken = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Grilled Chicken")
                .phonetic("/ɡrɪld ˈtʃɪk.ɪn/")
                .meaning("Gà nướng")
                .exampleSentence("Grilled chicken is delicious.")
                .imageUrl("/images/vocabulary/grilled-chicken.png")
                .difficulty("MEDIUM")
                .build());
        Vocabulary BeefSteak = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Beef Steak")
                .phonetic("/biːf steɪk/")
                .meaning("Bít tết bò")
                .exampleSentence("Beef steak is my favorite.")
                .imageUrl("/images/vocabulary/beef-steak.png")
                .difficulty("MEDIUM")
                .build());
        Vocabulary PorkChop = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Pork Chop")
                .phonetic("/pɔːk tʃɒp/")
                .meaning("Sườn heo")
                .exampleSentence("Pork chop is tasty.")
                .imageUrl("/images/vocabulary/pork-chop.png")
                .difficulty("MEDIUM")
                .build());
        //step 3
        Vocabulary ApplePie = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Apple Pie")
                .phonetic("/ˈæp.əl paɪ/")
                .meaning("Bánh táo")
                .exampleSentence("Apple pie is a classic dessert.")
                .imageUrl("/images/vocabulary/apple-pie.png")
                .difficulty("MEDIUM")
                .build());

        Vocabulary cheeseCake = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Cheesecake")
                .phonetic("/ˈtʃiːz.keɪk/")
                .meaning("Bánh phô mai")
                .exampleSentence("I love eating cheesecake.")
                .imageUrl("/images/vocabulary/cheesecake.png")
                .difficulty("MEDIUM")
                .build());
        Vocabulary chocolateCake = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Chocolate Cake")
                .phonetic("/ˈtʃɒk.lət keɪk/")
                .meaning("Bánh sô cô la")
                .exampleSentence("Chocolate cake is rich and sweet.")
                .imageUrl("/images/vocabulary/chocolate-cake.png")
                .difficulty("MEDIUM")
                .build());
        //step 4
        Vocabulary MilkShake = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Milkshake")
                .phonetic("/ˈmɪlk.ʃeɪk/")
                .meaning("Sữa lắc")
                .exampleSentence("I like strawberry milkshake.")
                .imageUrl("/images/vocabulary/milkshake.png")
                .difficulty("MEDIUM")
                .build());
        Vocabulary OrangeJuice = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Orange Juice")
                .phonetic("/ˈɒr.ɪndʒ dʒuːs/")
                .meaning("Nước cam")
                .exampleSentence("I drink orange juice every morning.")
                .imageUrl("/images/vocabulary/orange-juice.png")
                .difficulty("MEDIUM")
                .build());
        Vocabulary AppleJuice = vocabularyRepository.save(Vocabulary.builder()
                .category(foodCategory)
                .word("Apple Juice")
                .phonetic("/ˈæp.əl dʒuːs/")
                .meaning("Nước táo")
                .exampleSentence("Apple juice is refreshing.")
                .imageUrl("/images/vocabulary/apple-juice.png")
                .difficulty("MEDIUM")
                .build());
        scenarioVocabularyRepository.saveAll(List.of(
                ScenarioVocabulary.builder().scenario(FamilyRestaurant).vocabulary(BroccoliSoup).build(),
                ScenarioVocabulary.builder().scenario(FamilyRestaurant).vocabulary(MashedPotatoes).build(),
                ScenarioVocabulary.builder().scenario(FamilyRestaurant).vocabulary(GarlicBread).build(),
                ScenarioVocabulary.builder().scenario(FamilyRestaurant).vocabulary(GrilledChicken).build(),
                ScenarioVocabulary.builder().scenario(FamilyRestaurant).vocabulary(BeefSteak).build(),
                ScenarioVocabulary.builder().scenario(FamilyRestaurant).vocabulary(PorkChop).build(),
                ScenarioVocabulary.builder().scenario(FamilyRestaurant).vocabulary(ApplePie).build(),
                ScenarioVocabulary.builder().scenario(FamilyRestaurant).vocabulary(cheeseCake).build(),
                ScenarioVocabulary.builder().scenario(FamilyRestaurant).vocabulary(chocolateCake).build(),
                ScenarioVocabulary.builder().scenario(FamilyRestaurant).vocabulary(MilkShake).build(),
                ScenarioVocabulary.builder().scenario(FamilyRestaurant).vocabulary(OrangeJuice).build(),
                ScenarioVocabulary.builder().scenario(FamilyRestaurant).vocabulary(AppleJuice).build()
        ));
        scenarioStepRepository.saveAll(List.of(
                ScenarioStep.builder()
                        .scenario(FamilyRestaurant)
                        .stepOrder(1)
                        .buddyMessage("Mình muốn ăn món khai vị trước. Mình thích món làm từ Bread. Bạn giúp mình gọi nhé!")
                        .expectedIntent("ORDER_FOOD")
                        .expectedEntity("GARLIC_BREAD")
                        .successResponse("Đúng rồi! Garlic Bread là món mình muốn ăn.")
                        .failResponse("Hãy tìm món khai vị có Bread nhé!")
                        .build(),
                ScenarioStep.builder()
                        .scenario(FamilyRestaurant)
                        .stepOrder(2)
                        .buddyMessage("Giờ mình muốn món chính. Mình thích Chicken hơn các loại thịt khác.")
                        .expectedIntent("ORDER_FOOD")
                        .expectedEntity("GRILLED_CHICKEN")
                        .successResponse("Tuyệt quá! Món khoái khẩu của mình là Grilled Chicken!")
                        .failResponse("Hãy chọn món chính có Chicken nhé!")
                        .build(),
                ScenarioStep.builder()
                        .scenario(FamilyRestaurant)
                        .stepOrder(3)
                        .buddyMessage("Đến món tráng miệng rồi! Mình muốn món ngọt có Apple.")
                        .expectedIntent("ORDER_FOOD")
                        .expectedEntity("APPLE_PIE")
                        .successResponse("Tuyệt quá! Apple Pie là món mình thích nhất.")
                        .failResponse("Hãy tìm món tráng miệng có Apple nhé!")
                        .build(),
                ScenarioStep.builder()
                        .scenario(FamilyRestaurant)
                        .stepOrder(4)
                        .buddyMessage("Cuối cùng mình muốn một thức uống làm từ Milk.")
                        .expectedIntent("ORDER_DRINK")
                        .expectedEntity("MILKSHAKE")
                        .successResponse("Cảm ơn bạn! Bữa ăn của mình đã hoàn hảo rồi!")
                        .failResponse("Hãy chọn thức uống có Milk nhé!")
                        .build()
        ));
    }
}
