package com.billetera.api.service.impl;

import com.billetera.api.domain.model.User;
import com.billetera.api.dto.request.CreateUserRequest;
import com.billetera.api.dto.request.UpdateUserRequest;
import com.billetera.api.dto.response.UserResponse;
import com.billetera.api.exception.BusinessException;
import com.billetera.api.exception.ResourceNotFoundException;
import com.billetera.api.repository.UserRepository;
import com.billetera.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("El email ya se encuentra registrado");
        }

        User user = User.builder()
                .names(request.getNames())
                .lastNames(request.getLastNames())
                .email(request.getEmail())
                .status(true)
                .build();

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findByIdAndStatusTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado o inactivo con ID: " + id));

        user.setNames(request.getNames());
        user.setLastNames(request.getLastNames());

        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findByIdAndStatusTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado o inactivo con ID: " + id));
        return mapToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllActiveUsers() {
        return userRepository.findByStatusTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deactivateUser(Long id) {
        User user = userRepository.findByIdAndStatusTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado o inactivo con ID: " + id));

        user.setStatus(false); // Desactivación lógica
        userRepository.save(user);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .names(user.getNames())
                .lastNames(user.getLastNames())
                .email(user.getEmail())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}