# Architecture

## Tech Stack
- Backend: Java, Spring Boot
- Frontend: React, TypeScript
- Database: PostgreSQL
- ORM: Spring Data JPA (Hibernate)
- Build Tool: Maven
- Version Control: Git & GitHub
- Containerization: Docker (planned)

## Architecture Style
The application will initially follow a modular monolith architecture. As the project grows, selected modules may be extracted into microservices.

## Main Modules
- Authentication
- Users
- Hotels
- Rooms
- Bookings
- Reviews
- Notifications
- AI (future)

## Communication
The frontend communicates with the backend through REST APIs.

## Deployment
Initially the application will run locally using Docker. CI/CD and cloud deployment will be added in future iterations.
