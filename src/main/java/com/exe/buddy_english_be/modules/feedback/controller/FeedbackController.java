package com.exe.buddy_english_be.modules.feedback.controller;

import com.exe.buddy_english_be.modules.feedback.dto.FeedbackRequest;
import com.exe.buddy_english_be.modules.feedback.dto.FeedbackResponse;
import com.exe.buddy_english_be.modules.feedback.service.FeedbackService;
import com.exe.buddy_english_be.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("modulesFeedbackController")
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FeedbackResponse>> createFeedback(
            @Valid @RequestBody FeedbackRequest request) {
        FeedbackResponse response = feedbackService.createFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Feedback submitted successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FeedbackResponse>>> getAllFeedback() {
        List<FeedbackResponse> response = feedbackService.getAllFeedback();
        return ResponseEntity.ok(ApiResponse.success("Feedbacks fetched successfully", response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<FeedbackResponse>>> getFeedbackByUser(@PathVariable Long userId) {
        List<FeedbackResponse> response = feedbackService.getFeedbackByUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User feedbacks fetched successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FeedbackResponse>> getFeedbackById(@PathVariable Long id) {
        FeedbackResponse response = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(ApiResponse.success("Feedback fetched successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok(ApiResponse.success("Feedback deleted successfully", null));
    }
}
