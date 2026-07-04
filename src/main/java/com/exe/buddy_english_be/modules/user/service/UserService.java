package com.exe.buddy_english_be.modules.user.service;

import com.exe.buddy_english_be.modules.user.dto.CreateUserRequest;
import com.exe.buddy_english_be.modules.user.dto.UpdateUserRequest;
import com.exe.buddy_english_be.modules.user.dto.UpdateUserStatusRequest;
import com.exe.buddy_english_be.modules.user.dto.UserResponse;

import java.util.List;

public interface UserService {

    /** Get the currently authenticated user's own info. */
    UserResponse getCurrentUser(Long userId);

    /** Get a specific user by ID (admin). */
    UserResponse getUserById(Long id);

    /** List all users (admin). */
    List<UserResponse> getAllUsers();

    /** Create a new user (admin). */
    UserResponse createUser(CreateUserRequest request);

    /** Update a user's email and/or status (admin). */
    UserResponse updateUser(Long id, UpdateUserRequest request);

    /** Update only the status of a user (admin). */
    UserResponse updateUserStatus(Long id, UpdateUserStatusRequest request);

    /** Delete a user by ID (admin). */
    void deleteUser(Long id);
}
