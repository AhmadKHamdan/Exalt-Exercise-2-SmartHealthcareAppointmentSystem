package com.exalt.smarthealthcareappointmentsystem.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorObject {
    private Integer statusCode;
    private String message;
    private LocalDateTime timestamp;
}
