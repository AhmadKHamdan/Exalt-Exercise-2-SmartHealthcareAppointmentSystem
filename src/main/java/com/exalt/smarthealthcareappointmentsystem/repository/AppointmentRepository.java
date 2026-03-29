package com.exalt.smarthealthcareappointmentsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exalt.smarthealthcareappointmentsystem.entity.appointment.Appointment;
import com.exalt.smarthealthcareappointmentsystem.enums.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctorIdAndStatus(Long id, AppointmentStatus status);

    List<Appointment> findByPatientIdAndStatus(Long id, AppointmentStatus status);
}