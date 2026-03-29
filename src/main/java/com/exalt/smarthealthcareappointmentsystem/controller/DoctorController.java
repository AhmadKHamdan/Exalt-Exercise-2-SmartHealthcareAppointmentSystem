package com.exalt.smarthealthcareappointmentsystem.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exalt.smarthealthcareappointmentsystem.dto.response.doctor.DoctorResponse;
import com.exalt.smarthealthcareappointmentsystem.service.DoctorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "Get all doctors")
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getDoctors(@RequestParam(required = false) String specialty) {
        return ResponseEntity.ok(doctorService.getDoctors(specialty));
    }

    @Operation(summary = "Get a doctor by ID")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }
}
