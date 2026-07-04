package com.exe.buddy_english_be.config.seed;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.exe.buddy_english_be.modules.buddy.entity.BuddyProfile;
import com.exe.buddy_english_be.modules.buddy.enums.BuddyMood;
import com.exe.buddy_english_be.modules.buddy.repository.BuddyProfileRepository;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import com.exe.buddy_english_be.modules.user.entity.Role;
import com.exe.buddy_english_be.modules.user.entity.User;
import com.exe.buddy_english_be.modules.user.repository.RoleRepository;
import com.exe.buddy_english_be.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ChildProfileRepository childProfileRepository;
    private final BuddyProfileRepository buddyProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public ChildProfile seed() {
        User user = userRepository.save(User.builder()
                .email("testuser@buddy.com")
                .passwordHash(passwordEncoder.encode("password123"))
                .roles(Set.of(getChildRole()))
                .build());

        ChildProfile childProfile = childProfileRepository.save(ChildProfile.builder()
                .user(user)
                .nickname("testuser")
                .level(2)
                .xp(150)
                .coins(50)
                .streakDays(2)
                .lastLoginDate(java.time.LocalDate.now())
                .build());

        buddyProfileRepository.save(BuddyProfile.builder()
                .child(childProfile)
                .name("Buddy")
                .level(2)
                .friendshipPoints(20)
                .mood(BuddyMood.HAPPY)
                .energy(90)
                .lastInteractionAt(java.time.LocalDateTime.now())
                .build());

        return childProfile;
    }

    private Role getChildRole() {
        return roleRepository.findAll().stream()
                .filter(role -> "ROLE_CHILD".equals(role.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("ROLE_CHILD is missing"));
    }
}
