package com.exe.buddy_english_be.shared.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    VALIDATION_ERROR("Invalid request data", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS("Email or password is incorrect", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("Authentication is required", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    MISSION_NOT_FOUND("Mission not found", HttpStatus.NOT_FOUND),
    VOCABULARY_NOT_FOUND("Vocabulary not found", HttpStatus.NOT_FOUND),
    VOCABULARY_CATEGORY_NOT_FOUND("Vocabulary category not found", HttpStatus.NOT_FOUND),
    CHILD_PROFILE_NOT_FOUND("Child profile not found", HttpStatus.NOT_FOUND),
    WORLD_NOT_FOUND("World not found", HttpStatus.NOT_FOUND),
    SCENARIO_NOT_FOUND("Scenario not found", HttpStatus.NOT_FOUND),
    SCENARIO_STEP_NOT_FOUND("Scenario step not found", HttpStatus.NOT_FOUND),
    SCENARIO_VOCABULARY_NOT_FOUND("Scenario vocabulary not found", HttpStatus.NOT_FOUND),
    PROGRESS_NOT_FOUND("Progress not found", HttpStatus.NOT_FOUND),
    ACHIEVEMENT_NOT_FOUND("Achievement not found", HttpStatus.NOT_FOUND),
    CHILD_ACHIEVEMENT_NOT_FOUND("Child achievement not found", HttpStatus.NOT_FOUND),
    NOTIFICATION_NOT_FOUND("Notification not found", HttpStatus.NOT_FOUND),
    LEARNING_SESSION_NOT_FOUND("Learning session not found", HttpStatus.NOT_FOUND),
    WEEKLY_REPORT_NOT_FOUND("Weekly report not found", HttpStatus.NOT_FOUND),
    FEEDBACK_NOT_FOUND("Feedback not found", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS("Email is already registered", HttpStatus.CONFLICT),
    USERNAME_ALREADY_EXISTS("Username is already taken", HttpStatus.CONFLICT),
    INTERNAL_SERVER_ERROR("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
