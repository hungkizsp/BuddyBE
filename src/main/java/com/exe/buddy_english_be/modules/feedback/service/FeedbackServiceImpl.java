package com.exe.buddy_english_be.modules.feedback.service;

import com.exe.buddy_english_be.modules.feedback.dto.FeedbackRequest;
import com.exe.buddy_english_be.modules.feedback.dto.FeedbackResponse;
import com.exe.buddy_english_be.modules.feedback.entity.Feedback;
import com.exe.buddy_english_be.modules.feedback.repository.FeedbackRepository;
import com.exe.buddy_english_be.modules.user.entity.User;
import com.exe.buddy_english_be.modules.user.repository.UserRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public FeedbackResponse createFeedback(FeedbackRequest request) {
        User user = findUser(request.userId());
        Feedback feedback = Feedback.builder()
                .user(user)
                .rating(request.rating())
                .comment(request.comment())
                .createdAt(LocalDateTime.now())
                .build();
        return toResponse(feedbackRepository.save(feedback));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getAllFeedback() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getFeedbackByUser(Long userId) {
        return feedbackRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackResponse getFeedbackById(Long id) {
        return toResponse(findFeedback(id));
    }

    @Override
    @Transactional
    public void deleteFeedback(Long id) {
        feedbackRepository.delete(findFeedback(id));
    }

    // ---- helpers ----

    private Feedback findFeedback(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.FEEDBACK_NOT_FOUND));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    private FeedbackResponse toResponse(Feedback f) {
        return new FeedbackResponse(
                f.getId(),
                f.getUser() != null ? f.getUser().getId() : null,
                f.getUser() != null ? f.getUser().getEmail() : null,
                f.getRating(),
                f.getComment(),
                f.getCreatedAt()
        );
    }
}
