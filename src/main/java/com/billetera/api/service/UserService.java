package com.billetera.api.service;

import com.billetera.api.dto.request.CreateUserRequest;
import com.billetera.api.dto.request.UpdateUserRequest;
import com.billetera.api.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse updateUser(Long id, UpdateUserRequest request);
    UserResponse getUserById(Long id);
    List<UserResponse> getAllActiveUsers();
    void deactivateUser(Long id);
}