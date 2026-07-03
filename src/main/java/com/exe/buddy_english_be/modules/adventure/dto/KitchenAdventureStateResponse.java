package com.exe.buddy_english_be.modules.adventure.dto;

import java.util.List;

public record KitchenAdventureStateResponse(
        String gameState,
        List<KitchenAdventureItemDto> tableItems,
        int missionStage,
        boolean preparedEggOnToast,
        List<String> potContents,
        BuddyPositionDto buddyPosition,
        boolean draggingBuddy,
        String feedbackMessage,
        boolean missionPanelVisible,
        int xp,
        int coins,
        boolean showRewards,
        boolean listening,
        String missionInstruction,
        RewardDto reward
) {
}
