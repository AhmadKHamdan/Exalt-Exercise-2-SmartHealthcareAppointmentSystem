package com.exalt.smarthealthcareappointmentsystem.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exalt.smarthealthcareappointmentsystem.dto.response.DoctorResponse;
import com.exalt.smarthealthcareappointmentsystem.service.DoctorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getDoctors(@RequestParam(required = false) String specialty) {
        return ResponseEntity.ok(doctorService.getDoctors(specialty));
    }
}
