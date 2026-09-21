package com.exe.buddy_english_be.modules.auth.service;

import com.exe.buddy_english_be.modules.auth.dto.LoginRequest;
import com.exe.buddy_english_be.modules.auth.dto.LoginResponse;
import com.exe.buddy_english_be.modules.auth.dto.SignupRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request, HttpServletResponse response);
    LoginResponse loginWithGoogle(com.exe.buddy_english_be.modules.auth.dto.GoogleLoginRequest request, HttpServletResponse response);
    LoginResponse signup(SignupRequest request, HttpServletResponse response);
    LoginResponse verifyEmail(com.exe.buddy_english_be.modules.auth.dto.VerifyEmailRequest request, HttpServletResponse response);
    void sendVerificationOtp(String email);
    void forgotPassword(com.exe.buddy_english_be.modules.auth.dto.ForgotPasswordRequest request);
    void resetPassword(com.exe.buddy_english_be.modules.auth.dto.ResetPasswordRequest request);
    LoginResponse refreshToken(HttpServletRequest request, HttpServletResponse response);
    LoginResponse getCurrentUser(Long userId);
    void logout(HttpServletResponse response);
}
