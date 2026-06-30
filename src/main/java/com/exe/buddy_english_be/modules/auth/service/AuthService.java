package com.exe.buddy_english_be.modules.auth.service;

import com.exe.buddy_english_be.modules.auth.dto.LoginRequest;
import com.exe.buddy_english_be.modules.auth.dto.LoginResponse;
import com.exe.buddy_english_be.modules.auth.dto.SignupRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request, HttpServletResponse response);
    LoginResponse signup(SignupRequest request, HttpServletResponse response);
    LoginResponse getCurrentUser(Long userId);
    void logout(HttpServletResponse response);
}
