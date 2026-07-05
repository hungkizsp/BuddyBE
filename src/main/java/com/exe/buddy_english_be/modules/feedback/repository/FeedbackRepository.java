package com.exe.buddy_english_be.modules.feedback.repository;

import com.exe.buddy_english_be.modules.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByUserId(Long userId);

    List<Feedback> findAllByOrderByCreatedAtDesc();
}
