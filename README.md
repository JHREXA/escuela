# Self-Directed Learning Platform

Full-stack learning platform focused on autonomous learning with guided support.

Teachers can create and manage courses, modules, lessons and learning materials. Students can enroll in courses, track their progress, ask lesson-specific questions and request tutoring sessions when they need help.

The platform also includes scheduling logic such as teacher availability, time-off management, booking validation, cancellation, rescheduling and overlap prevention.

## Project Status

This project is currently in development as a personal learning project.

The main goal is to practice real-world backend architecture with Java, Spring Boot, Spring Security, PostgreSQL and Angular.

## Tech Stack

### Backend

- Java
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- PostgreSQL
- Swagger / OpenAPI

### Frontend

- Angular
- TypeScript
- HTML / CSS

### DevOps

- Docker
- Docker Compose

## Main Features

- User authentication with JWT
- Role-based access control
- Teacher and student roles
- Course management
- Module and lesson management
- Lesson materials
- Student enrollment
- Lesson progress tracking
- Lesson-specific questions
- Tutoring session requests
- Teacher availability management
- Time-off management
- Booking validation
- Cancellation and rescheduling
- Overlap prevention for tutoring sessions

## Backend Highlights

- Modular REST API design
- Role-based authentication and authorization
- Relational domain modeling with JPA/Hibernate
- Business rules for scheduling and tutoring sessions
- Booking conflict validation
- Protected access depending on user role
- API documentation with Swagger / OpenAPI

## Architecture

The backend follows a layered architecture:

- Controller layer: exposes REST endpoints
- Service layer: contains business logic
- Repository layer: handles database access
- Entity layer: represents the domain model
- DTO layer: separates API data from persistence entities
- Security layer: handles authentication and authorization

## Learning Goals

Through this project, I am improving my skills in:

- Building secure REST APIs with Spring Boot
- Implementing authentication and authorization with Spring Security and JWT
- Designing relational models with PostgreSQL and JPA
- Managing business rules in the service layer
- Structuring a full-stack application with Angular and Spring Boot
- Documenting APIs with Swagger
- Preparing applications for containerized environments with Docker
