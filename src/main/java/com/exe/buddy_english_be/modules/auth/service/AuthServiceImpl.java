package com.exe.buddy_english_be.modules.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.exe.buddy_english_be.modules.auth.dto.*;
import com.exe.buddy_english_be.modules.auth.entity.AuthOtp;
import com.exe.buddy_english_be.modules.auth.repository.AuthOtpRepository;
import com.exe.buddy_english_be.modules.user.entity.User;
import com.exe.buddy_english_be.modules.user.entity.Role;
import com.exe.buddy_english_be.modules.user.repository.UserRepository;
import com.exe.buddy_english_be.modules.user.repository.RoleRepository;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import com.exe.buddy_english_be.modules.buddy.entity.BuddyProfile;
import com.exe.buddy_english_be.modules.buddy.enums.BuddyMood;
import com.exe.buddy_english_be.modules.buddy.repository.BuddyProfileRepository;
import com.exe.buddy_english_be.security.CookieUtil;
import com.exe.buddy_english_be.security.JwtProvider;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ChildProfileRepository childProfileRepository;
    private final BuddyProfileRepository buddyProfileRepository;
    private final AuthOtpRepository authOtpRepository;
    private final AuthMailService authMailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            ChildProfileRepository childProfileRepository,
            BuddyProfileRepository buddyProfileRepository,
            AuthOtpRepository authOtpRepository,
            AuthMailService authMailService,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider,
            CookieUtil cookieUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.childProfileRepository = childProfileRepository;
        this.buddyProfileRepository = buddyProfileRepository;
        this.authOtpRepository = authOtpRepository;
        this.authMailService = authMailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.cookieUtil = cookieUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(request.email().trim().toLowerCase())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        user.getRoles().size();

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        cookieUtil.addAccessTokenCookie(response, accessToken);
        cookieUtil.addRefreshTokenCookie(response, refreshToken);

        return toLoginResponse(user, accessToken, refreshToken);
    }

    @Override
    @Transactional
    public LoginResponse loginWithGoogle(GoogleLoginRequest request, HttpServletResponse response) {
        String email = request.email() != null ? request.email().trim().toLowerCase() : "";
        String displayName = request.name() != null ? request.name().trim() : "";

        if ((email.isBlank() || displayName.isBlank()) && request.idToken() != null && request.idToken().contains(".")) {
            try {
                String[] parts = request.idToken().split("\\.");
                if (parts.length >= 2) {
                    String payloadJson = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
                    com.fasterxml.jackson.databind.JsonNode json = new com.fasterxml.jackson.databind.ObjectMapper().readTree(payloadJson);
                    if (email.isBlank() && json.has("email")) {
                        email = json.get("email").asText().trim().toLowerCase();
                    }
                    if (displayName.isBlank() && json.has("name")) {
                        displayName = json.get("name").asText().trim();
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to parse Google idToken payload: {}", e.getMessage());
            }
        }

        if (email.isBlank() && request.idToken() != null && request.idToken().contains("@")) {
            email = request.idToken().trim().toLowerCase();
        }

        if (email.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        if (displayName.isBlank()) {
            displayName = email.split("@")[0];
        }

        final String targetEmail = email;
        final String targetNickname = displayName;

        Role childRole = roleRepository.findByName("ROLE_CHILD")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_CHILD").build()));

        User user = userRepository.findByEmail(targetEmail).orElseGet(() -> {
            User newUser = User.builder()
                    .email(targetEmail)
                    .passwordHash(passwordEncoder.encode("GoogleAuth_" + System.currentTimeMillis()))
                    .emailVerified(true)
                    .authProvider("GOOGLE")
                    .roles(Set.of(childRole))
                    .build();
            newUser = userRepository.save(newUser);

            ChildProfile childProfile = ChildProfile.builder()
                    .user(newUser)
                    .nickname(targetNickname)
                    .level(1)
                    .xp(0)
                    .coins(0)
                    .streakDays(0)
                    .lastLoginDate(LocalDate.now())
                    .build();
            childProfile = childProfileRepository.save(childProfile);

            BuddyProfile buddyProfile = BuddyProfile.builder()
                    .child(childProfile)
                    .name("Buddy")
                    .level(1)
                    .friendshipPoints(0)
                    .mood(BuddyMood.HAPPY)
                    .energy(100)
                    .lastInteractionAt(LocalDateTime.now())
                    .build();
            buddyProfileRepository.save(buddyProfile);

            return newUser;
        });

        user.getRoles().size();

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        cookieUtil.addAccessTokenCookie(response, accessToken);
        cookieUtil.addRefreshTokenCookie(response, refreshToken);

        return toLoginResponse(user, accessToken, refreshToken);
    }

    @Override
    @Transactional
    public LoginResponse signup(SignupRequest request, HttpServletResponse response) {
        String email = request.email().trim().toLowerCase();
        String nickname = request.nickname().trim();

        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Role childRole = roleRepository.findByName("ROLE_CHILD")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_CHILD").build()));

        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(request.password()))
                .emailVerified(false)
                .authProvider("LOCAL")
                .roles(Set.of(childRole))
                .build();

        user = userRepository.save(user);

        // Create ChildProfile
        ChildProfile childProfile = ChildProfile.builder()
                .user(user)
                .nickname(nickname)
                .level(1)
                .xp(0)
                .coins(0)
                .streakDays(0)
                .lastLoginDate(LocalDate.now())
                .build();
        childProfileRepository.save(childProfile);

        // Create BuddyProfile
        BuddyProfile buddyProfile = BuddyProfile.builder()
                .child(childProfile)
                .name("Buddy")
                .level(1)
                .friendshipPoints(0)
                .mood(BuddyMood.HAPPY)
                .energy(100)
                .lastInteractionAt(LocalDateTime.now())
                .build();
        buddyProfileRepository.save(buddyProfile);

        // Send OTP
        sendVerificationOtp(email);

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        cookieUtil.addAccessTokenCookie(response, accessToken);
        cookieUtil.addRefreshTokenCookie(response, refreshToken);

        return toLoginResponse(user, accessToken, refreshToken);
    }

    @Override
    @Transactional
    public void sendVerificationOtp(String email) {
        String cleanEmail = email.trim().toLowerCase();
        String otp = generateOtp();
        
        AuthOtp authOtp = AuthOtp.builder()
                .email(cleanEmail)
                .otp(otp)
                .type("VERIFY_EMAIL")
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();
        authOtpRepository.save(authOtp);

        authMailService.sendVerificationOtp(cleanEmail, otp);
    }

    @Override
    @Transactional
    public LoginResponse verifyEmail(VerifyEmailRequest request, HttpServletResponse response) {
        String email = request.email().trim().toLowerCase();
        String otp = request.otp().trim();

        AuthOtp authOtp = authOtpRepository.findTopByEmailAndTypeAndUsedIsFalseAndExpiresAtAfterOrderByCreatedAtDesc(
                email, "VERIFY_EMAIL", LocalDateTime.now())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        if (!authOtp.getOtp().equals(otp)) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        authOtp.setUsed(true);
        authOtpRepository.save(authOtp);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.setEmailVerified(true);
        userRepository.save(user);

        user.getRoles().size();
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        cookieUtil.addAccessTokenCookie(response, accessToken);
        cookieUtil.addRefreshTokenCookie(response, refreshToken);

        return toLoginResponse(user, accessToken, refreshToken);
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        String email = request.email().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String otp = generateOtp();
        AuthOtp authOtp = AuthOtp.builder()
                .email(email)
                .otp(otp)
                .type("FORGOT_PASSWORD")
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();
        authOtpRepository.save(authOtp);

        authMailService.sendForgotPasswordOtp(user.getEmail(), otp);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        String email = request.email().trim().toLowerCase();
        String otp = request.otp().trim();

        AuthOtp authOtp = authOtpRepository.findTopByEmailAndTypeAndUsedIsFalseAndExpiresAtAfterOrderByCreatedAtDesc(
                email, "FORGOT_PASSWORD", LocalDateTime.now())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        if (!authOtp.getOtp().equals(otp)) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        authOtp.setUsed(true);
        authOtpRepository.save(authOtp);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieUtil.getRefreshToken(request);
        if (refreshToken == null || !jwtProvider.isTokenValid(refreshToken)) {
            cookieUtil.clearAuthCookies(response);
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        String tokenType = jwtProvider.getTokenType(refreshToken);
        if (!"REFRESH".equals(tokenType)) {
            cookieUtil.clearAuthCookies(response);
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        Long userId = jwtProvider.getUserId(refreshToken);
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);

        cookieUtil.addAccessTokenCookie(response, newAccessToken);
        cookieUtil.addRefreshTokenCookie(response, newRefreshToken);

        return toLoginResponse(user, newAccessToken, newRefreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return toLoginResponse(user);
    }

    @Override
    public void logout(HttpServletResponse response) {
        cookieUtil.clearAuthCookies(response);
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(900000) + 100000);
    }

    private LoginResponse toLoginResponse(User user) {
        return toLoginResponse(user, null, null);
    }

    private LoginResponse toLoginResponse(User user, String accessToken, String refreshToken) {
        Optional<ChildProfile> childProfileOpt = childProfileRepository.findByUserId(user.getId());

        LoginResponse.LoginResponseBuilder builder = LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .accessToken(accessToken)
                .refreshToken(refreshToken);

        if (childProfileOpt.isPresent()) {
            ChildProfile profile = childProfileOpt.get();
            builder.nickname(profile.getNickname())
                   .level(profile.getLevel())
                   .xp(profile.getXp())
                   .coins(profile.getCoins());
        } else {
            builder.nickname("User")
                   .level(0)
                   .xp(0)
                   .coins(0);
        }

        return builder.build();
    }
}
