package com.exe.buddy_english_be.modules.auth.repository;

import com.exe.buddy_english_be.modules.auth.entity.AuthOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AuthOtpRepository extends JpaRepository<AuthOtp, Long> {

    Optional<AuthOtp> findTopByEmailAndTypeAndUsedIsFalseAndExpiresAtAfterOrderByCreatedAtDesc(
            String email, String type, LocalDateTime now);
}
