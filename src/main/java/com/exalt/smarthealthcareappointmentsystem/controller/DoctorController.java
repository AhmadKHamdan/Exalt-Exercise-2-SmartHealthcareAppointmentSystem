package com.exalt.smarthealthcareappointmentsystem.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exalt.smarthealthcareappointmentsystem.dto.response.appointment.DoctorAppointmentResponse;
import com.exalt.smarthealthcareappointmentsystem.dto.response.doctor.DoctorResponse;
import com.exalt.smarthealthcareappointmentsystem.service.AppointmentService;
import com.exalt.smarthealthcareappointmentsystem.service.DoctorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    @Operation(summary = "Retrieve all doctors with optional specialty filter")
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getDoctors(@RequestParam(required = false) String specialty) {
        return ResponseEntity.ok(doctorService.getDoctors(specialty));
    }

    @Operation(summary = "Retrieve a specific doctor by ID")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @Operation(summary = "Retrieve all upcoming appointments for the current doctor")
    @GetMapping("/me/appointments")
    public ResponseEntity<List<DoctorAppointmentResponse>> getMyAppointments() {
        return ResponseEntity.ok(appointmentService.getAppointmentsForCurrentDoctor());
    }
}
