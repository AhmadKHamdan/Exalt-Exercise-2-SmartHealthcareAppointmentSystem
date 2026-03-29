package com.exalt.smarthealthcareappointmentsystem.dto.response.authentication;

public record LoginResponse(String jwtToken, String email, String role) {

}
