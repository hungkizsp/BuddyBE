package com.exe.buddy_english_be.config.seed;

import java.util.List;

import org.springframework.stereotype.Component;

import com.exe.buddy_english_be.modules.mission.entity.Mission;
import com.exe.buddy_english_be.modules.mission.enums.MissionFrequency;
import com.exe.buddy_english_be.modules.mission.enums.TargetType;
import com.exe.buddy_english_be.modules.mission.repository.MissionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MissionSeeder {

    private final MissionRepository missionRepository;

    public void seed() {
        missionRepository.saveAll(List.of(
                Mission.builder().title("Daily Talker").description("Speak 5 sentences to Buddy").missionFrequency(MissionFrequency.DAILY).targetType(TargetType.SPEAK_TIMES).targetValue(5).rewardCoin(10).rewardXp(15).build(),
                Mission.builder().title("Vocab Starter").description("Learn 3 new vocabulary words").missionFrequency(MissionFrequency.DAILY).targetType(TargetType.COMPLETE_ADVENTURE).targetValue(3).rewardCoin(15).rewardXp(20).build(),
                Mission.builder().title("Weekly Champion").description("Maintain a 7-day streak").missionFrequency(MissionFrequency.WEEKLY).targetType(TargetType.LOGIN_DAYS).targetValue(7).rewardCoin(50).rewardXp(100).build()
        ));
    }
}
