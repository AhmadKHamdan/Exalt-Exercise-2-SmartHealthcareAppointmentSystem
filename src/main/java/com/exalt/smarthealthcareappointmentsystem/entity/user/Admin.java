package com.exalt.smarthealthcareappointmentsystem.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "admins")
@Getter
@Setter
@SuperBuilder
public class Admin extends User {

    @Column(nullable = false)
    private String department;
}
