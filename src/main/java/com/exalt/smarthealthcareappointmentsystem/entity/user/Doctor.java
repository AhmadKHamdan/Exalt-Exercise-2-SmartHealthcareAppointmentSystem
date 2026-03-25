package com.exalt.smarthealthcareappointmentsystem.entity.user;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "doctors")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Doctor extends User {

    @Column(nullable = false)
    private String specialty;

    @Column(nullable = false)
    private LocalTime availabilityFrom;

    @Column(nullable = false)
    private LocalTime availabilityTill;
}
