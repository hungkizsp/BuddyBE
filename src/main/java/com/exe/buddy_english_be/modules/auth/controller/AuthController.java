package com.exe.buddy_english_be.modules.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exe.buddy_english_be.modules.auth.dto.LoginRequest;
import com.exe.buddy_english_be.modules.auth.dto.LoginResponse;
import com.exe.buddy_english_be.modules.auth.dto.SignupRequest;
import com.exe.buddy_english_be.modules.auth.service.AuthService;
import com.exe.buddy_english_be.shared.response.ApiResponse;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController("modulesAuthController")
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request, response);
        return ResponseEntity.ok(ApiResponse.success("Login successfully", loginResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> register(
            @Valid @RequestBody SignupRequest request,
            HttpServletResponse response) {
        LoginResponse signupResponse = authService.signup(request, response);
        return ResponseEntity.ok(ApiResponse.success("Registration successful", signupResponse));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<LoginResponse>> getCurrentUser(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        LoginResponse loginResponse = authService.getCurrentUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Current user fetched successfully", loginResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok(ApiResponse.success("Logout successfully", null));
    }
}
