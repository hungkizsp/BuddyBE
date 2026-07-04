package com.exe.buddy_english_be.config.seed;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.progress.entity.ChildVocabularyProgress;
import com.exe.buddy_english_be.modules.progress.repository.ChildVocabularyProgressRepository;
import com.exe.buddy_english_be.modules.vocabulary.entity.Vocabulary;
import com.exe.buddy_english_be.modules.vocabulary.entity.VocabularyCategory;
import com.exe.buddy_english_be.modules.vocabulary.repository.VocabularyCategoryRepository;
import com.exe.buddy_english_be.modules.vocabulary.repository.VocabularyRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VocabularySeeder {

    private final VocabularyCategoryRepository categoryRepository;
    private final VocabularyRepository vocabularyRepository;
    private final ChildVocabularyProgressRepository vocabularyProgressRepository;

    public VocabularySeedData seed(ChildProfile childProfile) {
        VocabularyCategory foodCat = categoryRepository.save(VocabularyCategory.builder().name("Food").build());
        VocabularyCategory animalCat = categoryRepository.save(VocabularyCategory.builder().name("Animals").build());
        VocabularyCategory emotionCat = categoryRepository.save(VocabularyCategory.builder().name("Emotions").build());
        VocabularyCategory placeCat = categoryRepository.save(VocabularyCategory.builder().name("Places").build());

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

        return new VocabularySeedData(foodCat, animalCat, emotionCat, placeCat);
    }

    public record VocabularySeedData(
            VocabularyCategory foodCategory,
            VocabularyCategory animalCategory,
            VocabularyCategory emotionCategory,
            VocabularyCategory placeCategory) {
    }
}
