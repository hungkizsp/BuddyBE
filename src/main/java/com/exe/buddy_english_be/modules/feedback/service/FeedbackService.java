package com.exe.buddy_english_be.modules.feedback.service;

import com.exe.buddy_english_be.modules.feedback.dto.FeedbackRequest;
import com.exe.buddy_english_be.modules.feedback.dto.FeedbackResponse;

import java.util.List;

public interface FeedbackService {

    FeedbackResponse createFeedback(FeedbackRequest request);

    List<FeedbackResponse> getAllFeedback();

    List<FeedbackResponse> getFeedbackByUser(Long userId);

    FeedbackResponse getFeedbackById(Long id);

    void deleteFeedback(Long id);
}
