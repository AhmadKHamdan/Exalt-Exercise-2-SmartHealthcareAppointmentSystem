package com.exalt.smarthealthcareappointmentsystem.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.exalt.smarthealthcareappointmentsystem.dto.request.authentication.LoginRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.authentication.LoginResponse;
import com.exalt.smarthealthcareappointmentsystem.entity.user.User;
import com.exalt.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.exalt.smarthealthcareappointmentsystem.repository.UserRepository;
import com.exalt.smarthealthcareappointmentsystem.security.JwtService;
import com.exalt.smarthealthcareappointmentsystem.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

        private final AuthenticationManager authenticationManager;
        private final UserRepository userRepository;
        private final JwtService jwtService;

        @Override
        public LoginResponse login(LoginRequest request) {
                authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(request.email(),
                                                request.password()));

                User user = userRepository.findByEmail(request.email()).orElseThrow(
                                () -> new ResourceNotFoundException("User not found with email: " + request.email()));

                String token = jwtService.generateToken(user);

                return new LoginResponse(token, user.getEmail(), user.getRole().name());
        }
}
