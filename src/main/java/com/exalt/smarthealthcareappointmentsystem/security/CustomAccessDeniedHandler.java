package com.exalt.smarthealthcareappointmentsystem.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String jsonResponse = String.format(
                "{\"statusCode\":%d,\"message\":\"%s\",\"timestamp\":\"%s\"}",
                HttpServletResponse.SC_FORBIDDEN, "Forbidden", LocalDateTime.now());

        response.getWriter().write(jsonResponse);
    }
}