package com.exe.buddy_english_be.modules.user.service;

import com.exe.buddy_english_be.modules.user.dto.UserResponse;
import com.exe.buddy_english_be.modules.user.dto.UpdateUserStatusRequest;

import java.util.List;

public interface UserService {

    UserResponse getCurrentUser(Long userId);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    UserResponse updateUserStatus(Long id, UpdateUserStatusRequest request);
}
