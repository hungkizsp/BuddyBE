package com.exe.buddy_english_be.config.seed;

import org.springframework.stereotype.Component;

import com.exe.buddy_english_be.modules.user.entity.Role;
import com.exe.buddy_english_be.modules.user.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleSeeder {

    private final RoleRepository roleRepository;

    public void seed() {
        roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
        roleRepository.save(Role.builder().name("ROLE_PARENT").build());
        roleRepository.save(Role.builder().name("ROLE_CHILD").build());
        roleRepository.save(Role.builder().name("ROLE_TEACHER").build());
    }
}
