package com.exe.buddy_english_be.shared.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    VALIDATION_ERROR("Invalid request data", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS("Email or password is incorrect", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("Authentication is required", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
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
