package com.exalt.smarthealthcareappointmentsystem;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.exalt.smarthealthcareappointmentsystem.entity.user.Admin;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Doctor;
import com.exalt.smarthealthcareappointmentsystem.entity.user.Patient;
import com.exalt.smarthealthcareappointmentsystem.repository.AdminRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.DoctorRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.PatientRepository;

@SpringBootApplication
public class SmartHealthcareAppointmentSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartHealthcareAppointmentSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner run(DoctorRepository doctorRepo, PatientRepository patientRepository,
            AdminRepository adminRepository) {
        return args -> {

            Doctor doctor = Doctor.builder()
                    .email("doctor@mail.com")
                    .username("doctor")
                    .password("123")
                    .specialty("Nerves")
                    .availabilityFrom(LocalTime.of(8, 30))
                    .availabilityTill(LocalTime.of(16, 0))
                    .build();

            doctorRepo.save(doctor);

            Patient patient = Patient.builder()
                    .email("patient@mail.com")
                    .username("patient")
                    .password("123")
                    .dateOfBirth(LocalDate.now())
                    .build();

            patientRepository.save(patient);

            Admin admin = Admin.builder()
                    .email("admin@mail.com")
                    .username("admin")
                    .password("123")
                    .department("IT")
                    .build();

            adminRepository.save(admin);
        };
    }
}
