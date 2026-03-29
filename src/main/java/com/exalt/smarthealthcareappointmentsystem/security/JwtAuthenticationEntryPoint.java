package com.exalt.smarthealthcareappointmentsystem.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException {

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    String jsonResponse = String.format(
        "{\"statusCode\":%d,\"message\":\"%s\",\"timestamp\":\"%s\"}",
        HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", LocalDateTime.now());

    response.getWriter().write(jsonResponse);
  }
}