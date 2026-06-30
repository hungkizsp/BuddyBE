package com.exe.buddy_english_be.modules.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.exe.buddy_english_be.modules.auth.dto.LoginRequest;
import com.exe.buddy_english_be.modules.auth.dto.LoginResponse;
import com.exe.buddy_english_be.modules.auth.dto.SignupRequest;
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

import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ChildProfileRepository childProfileRepository;
    private final BuddyProfileRepository buddyProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            ChildProfileRepository childProfileRepository,
            BuddyProfileRepository buddyProfileRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider,
            CookieUtil cookieUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.childProfileRepository = childProfileRepository;
        this.buddyProfileRepository = buddyProfileRepository;
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

        // Trigger fetch of roles before building JWT
        user.getRoles().size();

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        cookieUtil.addAccessTokenCookie(response, accessToken);
        cookieUtil.addRefreshTokenCookie(response, refreshToken);

        return toLoginResponse(user);
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
        childProfile = childProfileRepository.save(childProfile);

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

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        cookieUtil.addAccessTokenCookie(response, accessToken);
        cookieUtil.addRefreshTokenCookie(response, refreshToken);

        return toLoginResponse(user);
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

    private LoginResponse toLoginResponse(User user) {
        Optional<ChildProfile> childProfileOpt = childProfileRepository.findByUserId(user.getId());

        LoginResponse.LoginResponseBuilder builder = LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

        if (childProfileOpt.isPresent()) {
            ChildProfile profile = childProfileOpt.get();
            builder.nickname(profile.getNickname())
                   .level(profile.getLevel())
                   .xp(profile.getXp())
                   .coins(profile.getCoins());
        } else {
            // For admin / parents who do not have a child profile
            builder.nickname("User")
                   .level(0)
                   .xp(0)
                   .coins(0);
        }

        return builder.build();
    }
}
