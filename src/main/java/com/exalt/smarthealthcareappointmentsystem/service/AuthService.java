package com.exalt.smarthealthcareappointmentsystem.service;

import com.exalt.smarthealthcareappointmentsystem.dto.request.authentication.LoginRequest;
import com.exalt.smarthealthcareappointmentsystem.dto.response.authentication.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}
