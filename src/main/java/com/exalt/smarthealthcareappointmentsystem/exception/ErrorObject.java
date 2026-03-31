package com.exalt.smarthealthcareappointmentsystem.exception;

import java.time.LocalDateTime;

public record ErrorObject(Integer statusCode, String message, LocalDateTime timestamp) {
    
}
