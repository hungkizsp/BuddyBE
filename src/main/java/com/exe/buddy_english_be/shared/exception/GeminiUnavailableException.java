package com.exe.buddy_english_be.shared.exception;

/**
 * Thrown when the Gemini API is unavailable due to HTTP 5xx errors,
 * network failures, timeouts, missing API key, or unexpected API failures.
 */
public class GeminiUnavailableException extends RuntimeException {

    public GeminiUnavailableException(String message) {
        super(message);
    }

    public GeminiUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
