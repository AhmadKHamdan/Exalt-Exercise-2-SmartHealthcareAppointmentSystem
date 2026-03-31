# 🏥 Smart Healthcare Appointment System

A full-featured backend system for managing healthcare appointments,
medical records, and user roles (Admin, Doctor, Patient).\
Built using **Spring Boot**, with secure authentication, role-based
access control, and efficient data handling.

------------------------------------------------------------------------

## 🚀 Features

### 👤 Authentication & Security

-   JWT-based authentication
-   Role-based authorization (`ADMIN`, `DOCTOR`, `PATIENT`)
-   Secure password hashing using BCrypt
-   Custom access denied & authentication handlers

### 🧑‍⚕️ Doctor Management

-   Create, update, delete doctors (Admin)
-   Retrieve doctors with optional specialty filtering
-   View doctor availability

### 🧑 Patient Management

-   Create, update, delete patients (Admin)
-   Patients can update their own profile
-   View personal appointment history

### 📅 Appointment Management

-   Book appointments with doctors
-   Conflict detection (time overlap, availability)
-   Cancel appointments
-   Mark appointments as completed (Admin)

### 📄 Medical Records

-   Create records for completed appointments
-   MongoDB-based storage for flexibility
-   Access control for doctors and patients
-   Retrieve detailed and summary records

### 📊 Logging (AOP)

-   Automatic logging of actions (booking, record creation, etc.)
-   Includes user, role, and method metadata

### ⚡ Caching

-   Hibernate Second-Level Cache (Ehcache)
-   Spring Cache for optimized read operations (doctors)

------------------------------------------------------------------------

## 🏗️ Architecture

Controller → Service → Repository → Database

------------------------------------------------------------------------

## 🛠️ Tech Stack

-   Spring Boot
-   Spring Security + JWT
-   MySQL
-   MongoDB
-   Hibernate / JPA
-   Ehcache
-   Swagger (OpenAPI)
-   Maven

------------------------------------------------------------------------

## 🔐 Roles

-   ADMIN
-   DOCTOR
-   PATIENT

------------------------------------------------------------------------

## ⚙️ Setup

### Clone

    git clone <your-repo-url>
    cd smart-healthcare-appointment-system

### Configure

    spring.datasource.url=jdbc:mysql://localhost:3306/smart_healthcare
    spring.datasource.username=root
    spring.datasource.password=your_password

    spring.data.mongodb.uri=mongodb://localhost:27017/smart_healthcare

    jwt.secret=your_secret_key
    jwt.expiration=70000000

### Run

    mvn clean install
    mvn spring-boot:run

------------------------------------------------------------------------

## 📘 Swagger

http://localhost:8080/swagger-ui/index.html

