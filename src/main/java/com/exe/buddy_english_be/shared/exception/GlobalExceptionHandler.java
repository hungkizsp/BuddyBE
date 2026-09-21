package com.exe.buddy_english_be.shared.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.exe.buddy_english_be.shared.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(AsyncRequestNotUsableException.class)
        public void handleAsyncRequestNotUsableException(AsyncRequestNotUsableException exception) {
                // Client disconnected from SSE or async stream; ignore gracefully without attempting to write response
        }

        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException exception) {
                ErrorCode errorCode = exception.getErrorCode();
                return ResponseEntity
                                .status(errorCode.getStatus())
                                .body(ApiResponse.error(errorCode.getMessage(), null));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
                        MethodArgumentNotValidException exception) {
                Map<String, String> errors = new LinkedHashMap<>();
                exception.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                return ResponseEntity
                                .badRequest()
                                .body(ApiResponse.error(ErrorCode.VALIDATION_ERROR.getMessage(), errors));
        }

        @ExceptionHandler({ AuthenticationException.class, AccessDeniedException.class })
        public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException(Exception exception) {
                return ResponseEntity
                                .status(ErrorCode.UNAUTHORIZED.getStatus())
                                .body(ApiResponse.error(ErrorCode.UNAUTHORIZED.getMessage(), null));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<String>> handleUnexpectedException(Exception exception) {
                exception.printStackTrace();
                String errorDetail = exception.getClass().getSimpleName() + ": " + (exception.getMessage() != null ? exception.getMessage() : "Unknown error");

                return ResponseEntity
                                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                                .body(ApiResponse.error(errorDetail, errorDetail));
        }
}
