package com.exalt.smarthealthcareappointmentsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exalt.smarthealthcareappointmentsystem.entity.user.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}