package com.exe.buddy_english_be.modules.adventure.service;

import com.exe.buddy_english_be.modules.adventure.dto.BuddyPositionDto;
import com.exe.buddy_english_be.modules.adventure.dto.KitchenAdventureActionResponse;
import com.exe.buddy_english_be.modules.adventure.dto.KitchenAdventureStateResponse;
import com.exe.buddy_english_be.modules.adventure.dto.ToggleStateRequest;

public interface KitchenAdventureService {

    KitchenAdventureStateResponse getState(Long userId);

    KitchenAdventureActionResponse startMission(Long userId);

    KitchenAdventureActionResponse arriveAtTable(Long userId);

    KitchenAdventureActionResponse dropOnBuddy(Long userId, String itemId);

    KitchenAdventureActionResponse combineItems(Long userId, String draggedId, String targetId);

    KitchenAdventureActionResponse dropOnPot(Long userId, String itemId);

    KitchenAdventureActionResponse updateBuddyPosition(Long userId, BuddyPositionDto position);

    KitchenAdventureActionResponse toggleMic(Long userId, ToggleStateRequest request);

    KitchenAdventureActionResponse toggleMissionPanel(Long userId, ToggleStateRequest request);

    KitchenAdventureActionResponse closeRewards(Long userId);
}
