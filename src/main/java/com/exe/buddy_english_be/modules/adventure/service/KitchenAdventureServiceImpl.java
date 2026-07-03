package com.exe.buddy_english_be.modules.adventure.service;

import com.exe.buddy_english_be.modules.adventure.dto.BuddyPositionDto;
import com.exe.buddy_english_be.modules.adventure.dto.KitchenAdventureActionResponse;
import com.exe.buddy_english_be.modules.adventure.dto.KitchenAdventureItemDto;
import com.exe.buddy_english_be.modules.adventure.dto.KitchenAdventureStateResponse;
import com.exe.buddy_english_be.modules.adventure.dto.RewardDto;
import com.exe.buddy_english_be.modules.adventure.dto.ToggleStateRequest;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KitchenAdventureServiceImpl implements KitchenAdventureService {
    private static final String NOT_STARTED = "not-started";
    private static final String WALKING_TO_TABLE = "walking-to-table";
    private static final String IDLE_AT_TABLE = "idle-at-table";
    private static final String COMPLETED = "completed";
    private static final int DEFAULT_XP = 60;
    private static final int DEFAULT_COINS = 15;
    private static final int XP_REWARD = 20;
    private static final int COIN_REWARD = 10;
    private static final RewardDto REWARD = new RewardDto(XP_REWARD, COIN_REWARD);

    private static final KitchenAdventureItemDto BANANA = new KitchenAdventureItemDto(
            "banana",
            "Banana",
            "\uD83C\uDF4C",
            "banana",
            "Banana"
    );
    private static final KitchenAdventureItemDto EGG = new KitchenAdventureItemDto(
            "egg",
            "Egg",
            "\uD83E\uDD5A",
            "egg",
            "Egg"
    );
    private static final KitchenAdventureItemDto BREAD = new KitchenAdventureItemDto(
            "bread",
            "Bread",
            "\uD83C\uDF5E",
            "bread",
            "Bread"
    );
    private static final KitchenAdventureItemDto MILK = new KitchenAdventureItemDto(
            "milk",
            "Milk",
            "\uD83E\uDD5B",
            "milk",
            "Milk"
    );
    private static final KitchenAdventureItemDto EGG_ON_TOAST = new KitchenAdventureItemDto(
            "egg-on-toast",
            "Egg on Toast",
            "\uD83C\uDF73",
            "egg-on-toast",
            "Egg on Toast"
    );
    private static final KitchenAdventureItemDto APPLE = new KitchenAdventureItemDto(
            "apple",
            "Apple",
            "\uD83C\uDF4E",
            "apple",
            "Apple"
    );

    private final ChildProfileRepository childProfileRepository;
    private final Map<Long, KitchenAdventureSession> sessions = new ConcurrentHashMap<>();

    public KitchenAdventureServiceImpl(ChildProfileRepository childProfileRepository) {
        this.childProfileRepository = childProfileRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public KitchenAdventureStateResponse getState(Long userId) {
        return toResponse(getOrCreateSession(userId));
    }

    @Override
    public KitchenAdventureActionResponse startMission(Long userId) {
        KitchenAdventureSession session = getOrCreateSession(userId);
        synchronized (session) {
            if (!NOT_STARTED.equals(session.gameState)) {
                return action(false, session);
            }

            session.gameState = WALKING_TO_TABLE;
            session.tableItems = initialTableItems();
            session.missionStage = 0;
            session.preparedEggOnToast = false;
            session.potContents.clear();
            session.buddyPosition = new BuddyPositionDto("47%", "55%");
            session.feedbackMessage = "Buddy is walking to the kitchen table. Combine bread and egg in the pot to make egg on toast.";
            session.showRewards = false;
            session.rewardGranted = false;
            return action(true, session);
        }
    }

    @Override
    public KitchenAdventureActionResponse arriveAtTable(Long userId) {
        KitchenAdventureSession session = getOrCreateSession(userId);
        synchronized (session) {
            if (!WALKING_TO_TABLE.equals(session.gameState)) {
                return action(false, session);
            }

            session.gameState = IDLE_AT_TABLE;
            return action(true, session);
        }
    }

    @Override
    @Transactional
    public KitchenAdventureActionResponse dropOnBuddy(Long userId, String itemId) {
        KitchenAdventureSession session = getOrCreateSession(userId);
        synchronized (session) {
            if (!IDLE_AT_TABLE.equals(session.gameState)) {
                return action(false, session);
            }

            if ("banana".equals(itemId)) {
                session.feedbackMessage = "Buddy: Today I don't want banana. Try something else.";
                return action(false, session);
            }

            if ("bread".equals(itemId) || "egg".equals(itemId)) {
                session.feedbackMessage = "Buddy: I want egg on toast, not plain bread or egg. Combine them first.";
                return action(false, session);
            }

            if ("egg-on-toast".equals(itemId)) {
                if (session.missionStage == 0) {
                    session.missionStage = 1;
                    session.feedbackMessage = "Buddy: Egg on toast is perfect! Now please give me milk.";
                    removeItem(session, "egg-on-toast");
                    return action(true, session);
                }
                return action(false, session);
            }

            if ("milk".equals(itemId)) {
                if (session.missionStage == 0) {
                    session.feedbackMessage = "Buddy: I need egg on toast first.";
                    return action(false, session);
                }
                if (session.missionStage == 1) {
                    session.missionStage = 2;
                    session.feedbackMessage = "Buddy: Milk is refreshing! Now the apple is unlocked.";
                    removeItem(session, "milk");
                    addItemIfMissing(session, APPLE);
                    return action(true, session);
                }
                return action(false, session);
            }

            if ("apple".equals(itemId)) {
                if (session.missionStage == 0) {
                    session.feedbackMessage = "Buddy: Egg on toast first, then milk, then apple.";
                    return action(false, session);
                }
                if (session.missionStage == 1) {
                    session.feedbackMessage = "Buddy: I want milk before apple.";
                    return action(false, session);
                }
                if (session.missionStage == 2) {
                    completeMission(userId, session);
                    return action(true, session);
                }
            }

            session.feedbackMessage = "Buddy: That does not look like the food I need right now.";
            return action(false, session);
        }
    }

    @Override
    public KitchenAdventureActionResponse combineItems(Long userId, String draggedId, String targetId) {
        KitchenAdventureSession session = getOrCreateSession(userId);
        synchronized (session) {
            if (!IDLE_AT_TABLE.equals(session.gameState)) {
                return action(false, session);
            }
            if (session.missionStage != 0 || session.preparedEggOnToast) {
                session.feedbackMessage = "Buddy is waiting for food in the right order.";
                return action(false, session);
            }

            Set<String> pair = new HashSet<>(List.of(draggedId, targetId));
            if (pair.contains("bread") && pair.contains("egg")) {
                session.preparedEggOnToast = true;
                session.feedbackMessage = "Great! You made egg on toast. Feed it to Buddy now.";
                removeItem(session, "bread");
                removeItem(session, "egg");
                addItemIfMissing(session, EGG_ON_TOAST);
                return action(true, session);
            }

            session.feedbackMessage = "Those items cannot be combined. Try bread and egg.";
            return action(false, session);
        }
    }

    @Override
    public KitchenAdventureActionResponse dropOnPot(Long userId, String itemId) {
        KitchenAdventureSession session = getOrCreateSession(userId);
        synchronized (session) {
            if (!IDLE_AT_TABLE.equals(session.gameState)) {
                return action(false, session);
            }

            if (!"bread".equals(itemId) && !"egg".equals(itemId)) {
                session.feedbackMessage = "Only bread and egg can go into the pot to make egg on toast.";
                return action(false, session);
            }

            if (session.potContents.contains(itemId)) {
                session.feedbackMessage = "That " + itemId + " is already in the pot.";
                return action(false, session);
            }

            session.potContents.add(itemId);
            removeItem(session, itemId);

            if (session.potContents.contains("bread") && session.potContents.contains("egg")) {
                session.preparedEggOnToast = true;
                session.potContents.clear();
                addItemIfMissing(session, EGG_ON_TOAST);
                session.feedbackMessage = "Egg on toast is ready! Drag it to Buddy now.";
                return action(true, session);
            }

            session.feedbackMessage = "Added " + itemId + " to the pot. Add the other ingredient.";
            return action(true, session);
        }
    }

    @Override
    public KitchenAdventureActionResponse updateBuddyPosition(Long userId, BuddyPositionDto position) {
        KitchenAdventureSession session = getOrCreateSession(userId);
        synchronized (session) {
            session.buddyPosition = position;
            return action(true, session);
        }
    }

    @Override
    public KitchenAdventureActionResponse toggleMic(Long userId, ToggleStateRequest request) {
        KitchenAdventureSession session = getOrCreateSession(userId);
        synchronized (session) {
            session.listening = request != null && request.value() != null
                    ? request.value()
                    : !session.listening;
            return action(true, session);
        }
    }

    @Override
    public KitchenAdventureActionResponse toggleMissionPanel(Long userId, ToggleStateRequest request) {
        KitchenAdventureSession session = getOrCreateSession(userId);
        synchronized (session) {
            session.missionPanelVisible = request != null && request.value() != null
                    ? request.value()
                    : !session.missionPanelVisible;
            return action(true, session);
        }
    }

    @Override
    public KitchenAdventureActionResponse closeRewards(Long userId) {
        KitchenAdventureSession session = getOrCreateSession(userId);
        synchronized (session) {
            session.showRewards = false;
            return action(true, session);
        }
    }

    private void completeMission(Long userId, KitchenAdventureSession session) {
        session.missionStage = 3;
        session.gameState = COMPLETED;
        session.feedbackMessage = "Buddy: Yummy! Thank you!";
        session.showRewards = true;
        removeItem(session, "apple");

        if (session.rewardGranted) {
            return;
        }

        boolean profileUpdated = childProfileRepository.findByUserId(userId).map(child -> {
            child.setXp(safe(child.getXp()) + XP_REWARD);
            child.setCoins(safe(child.getCoins()) + COIN_REWARD);
            childProfileRepository.save(child);
            session.xp = child.getXp();
            session.coins = child.getCoins();
            return true;
        }).orElse(false);

        if (!profileUpdated) {
            session.xp += XP_REWARD;
            session.coins += COIN_REWARD;
        }
        session.rewardGranted = true;
    }

    private KitchenAdventureSession getOrCreateSession(Long userId) {
        return sessions.computeIfAbsent(userId, this::newSession);
    }

    private KitchenAdventureSession newSession(Long userId) {
        KitchenAdventureSession session = new KitchenAdventureSession();
        childProfileRepository.findByUserId(userId).ifPresentOrElse(child -> {
            session.xp = safe(child.getXp());
            session.coins = safe(child.getCoins());
        }, () -> {
            session.xp = DEFAULT_XP;
            session.coins = DEFAULT_COINS;
        });
        return session;
    }

    private KitchenAdventureActionResponse action(boolean accepted, KitchenAdventureSession session) {
        return new KitchenAdventureActionResponse(accepted, toResponse(session));
    }

    private KitchenAdventureStateResponse toResponse(KitchenAdventureSession session) {
        return new KitchenAdventureStateResponse(
                session.gameState,
                List.copyOf(session.tableItems),
                session.missionStage,
                session.preparedEggOnToast,
                List.copyOf(session.potContents),
                session.buddyPosition,
                false,
                session.feedbackMessage,
                session.missionPanelVisible,
                session.xp,
                session.coins,
                session.showRewards,
                session.listening,
                missionInstruction(session),
                REWARD
        );
    }

    private String missionInstruction(KitchenAdventureSession session) {
        if (NOT_STARTED.equals(session.gameState)) {
            return "Click 'Start Mission' to begin the kitchen adventure.";
        }
        if (WALKING_TO_TABLE.equals(session.gameState)) {
            return "Buddy is walking to the table...";
        }
        if (IDLE_AT_TABLE.equals(session.gameState)) {
            if (session.missionStage == 0) {
                return "Combine bread + egg into egg on toast, then feed Buddy milk. Banana is rejected.";
            }
            if (session.missionStage == 1) {
                return "Buddy ate egg on toast! Give him milk now to unlock the apple.";
            }
            if (session.missionStage == 2) {
                return "One more step: give Buddy the apple to complete the mission.";
            }
        }
        return "Mission complete! Buddy is happy and full!";
    }

    private static List<KitchenAdventureItemDto> initialTableItems() {
        return new ArrayList<>(List.of(BANANA, EGG, BREAD, MILK));
    }

    private static void removeItem(KitchenAdventureSession session, String itemId) {
        session.tableItems.removeIf(item -> item.id().equals(itemId));
    }

    private static void addItemIfMissing(KitchenAdventureSession session, KitchenAdventureItemDto item) {
        boolean exists = session.tableItems.stream().anyMatch(existing -> existing.id().equals(item.id()));
        if (!exists) {
            session.tableItems.add(item);
        }
    }

    private static int safe(Integer value) {
        return value == null ? 0 : value;
    }

    private static class KitchenAdventureSession {
        private String gameState = NOT_STARTED;
        private List<KitchenAdventureItemDto> tableItems = initialTableItems();
        private int missionStage = 0;
        private boolean preparedEggOnToast = false;
        private final List<String> potContents = new ArrayList<>();
        private BuddyPositionDto buddyPosition = new BuddyPositionDto("47%", "55%");
        private String feedbackMessage = "Buddy is waiting. Start the kitchen mission to begin.";
        private boolean missionPanelVisible = true;
        private int xp = DEFAULT_XP;
        private int coins = DEFAULT_COINS;
        private boolean showRewards = false;
        private boolean listening = false;
        private boolean rewardGranted = false;
    }
}
