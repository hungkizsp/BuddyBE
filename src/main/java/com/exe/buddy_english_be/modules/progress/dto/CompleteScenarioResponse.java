package com.exe.buddy_english_be.modules.progress.dto;

/**
 * Returned after a scenario is completed.
 *
 * @param alreadyCompleted true when the child had already finished this scenario before this call —
 *                         the frontend uses this flag to decide whether to award XP/coins.
 * @param scenarioProgress updated scenario progress record
 * @param worldProgress    updated world progress record (may be null if the world record could not be resolved)
 */
public record CompleteScenarioResponse(
        boolean alreadyCompleted,
        ChildScenarioProgressResponse scenarioProgress,
        ChildWorldProgressResponse worldProgress
) {
}
