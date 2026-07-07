package com.exe.buddy_english_be.shared.exception;

/**
 * Thrown when the Gemini API returns HTTP 429 or a RESOURCE_EXHAUSTED error,
 * indicating that the API quota has been exceeded.
 */
public class GeminiQuotaExceededException extends RuntimeException {

    public GeminiQuotaExceededException(String message) {
        super(message);
    }

    public GeminiQuotaExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
