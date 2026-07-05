package com.exe.buddy_english_be.config.seed;

import java.util.List;

import org.springframework.stereotype.Component;

import com.exe.buddy_english_be.modules.reward.entity.Reward;
import com.exe.buddy_english_be.modules.reward.enums.RewardRarity;
import com.exe.buddy_english_be.modules.reward.enums.RewardType;
import com.exe.buddy_english_be.modules.reward.repository.RewardRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RewardSeeder {

    private final RewardRepository rewardRepository;

    public void seed() {
        rewardRepository.saveAll(List.of(
                Reward.builder().name("Pirate Hat").type(RewardType.ITEM).price(30).rarity(RewardRarity.RARE).isLimited(false).imageUrl("pirate_hat.png").build(),
                Reward.builder().name("Red Cape").type(RewardType.ITEM).price(20).rarity(RewardRarity.COMMON).isLimited(false).imageUrl("red_cape.png").build(),
                Reward.builder().name("Golden Crown").type(RewardType.ITEM).price(100).rarity(RewardRarity.LEGENDARY).isLimited(true).availableFrom(java.time.LocalDateTime.now().minusDays(1)).availableTo(java.time.LocalDateTime.now().plusDays(30)).imageUrl("golden_crown.png").build()
        ));
    }
}
