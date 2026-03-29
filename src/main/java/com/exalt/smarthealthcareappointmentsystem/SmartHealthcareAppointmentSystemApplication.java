package com.exalt.smarthealthcareappointmentsystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.exalt.smarthealthcareappointmentsystem.entity.user.Admin;
import com.exalt.smarthealthcareappointmentsystem.enums.Role;
import com.exalt.smarthealthcareappointmentsystem.repository.AdminRepository;
import com.exalt.smarthealthcareappointmentsystem.repository.UserRepository;

@SpringBootApplication
public class SmartHealthcareAppointmentSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartHealthcareAppointmentSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner seedAdmin(AdminRepository adminRepository, UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@system.com").isEmpty()) {
                Admin admin = Admin.builder()
                        .email("admin@system.com")
                        .fullName("System Admin")
                        .password(passwordEncoder.encode("admin123"))
                        .role(Role.ROLE_ADMIN)
                        .department("Management")
                        .build();
                adminRepository.save(admin);
            }
        };
    }
}
