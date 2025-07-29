# URL Shortener Backend

This is a beginner-friendly URL Shortener backend project built with **Spring Boot (Kotlin)** and **PostgreSQL**.  
I created this as part of a coding challenge while learning backend development and practicing clean architecture and testing.

---

## What this project does

- Shorten a long URL into a short 6-character code.
- Retrieve the original URL using the short code.
- List all stored URLs.
- Includes basic input validation and error handling.

---

## Technologies I used

- **Kotlin** – Programming language
- **Spring Boot** – To create REST APIs
- **Spring Data JPA** – For database interactions
- **PostgreSQL** – Database
- **Docker** – To run PostgreSQL in a container
- **JUnit 5 + MockK + MockMvc** – For testing
- **Gradle (Kotlin DSL)** – Build tool

---

## How the project is organized

- `controller/` → Handles API requests (endpoints)
- `service/` → Contains business logic
- `repository/` → Connects to the database
- `model/` → Database entity
- `dto/` → Request and response objects
- `exception/` → Custom exceptions and error handling

There is also a **test** folder where I wrote unit tests and integration tests.

---

## How to run this project (beginner steps)

1. **Start PostgreSQL in Docker**
    - If the container is already created, just start it:  
      `docker start urlshortener-db`
    - If not, create it for the first time:  
      `docker run --name urlshortener-db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=urlshortener -p 5432:5432 -d postgres:16`

2. **Run the application**
    - Open the project in IntelliJ and run `UrlshortenerApplication.kt`, **or** use terminal:  
      `./gradlew bootRun`

3. **Test the API endpoints using Postman**
    - Shorten URL: `POST http://localhost:8080/api/shorten`  
      Body: `{ "url": "https://example.com" }`
    - Retrieve original: `GET http://localhost:8080/api/original/{shortCode}`
    - List all URLs: `GET http://localhost:8080/api/urls`

---

## How I tested it

- Wrote **unit tests** for service and controller using mocks (MockK and MockMvc).
- Wrote **repository tests** using real PostgreSQL (test profile).
- Wrote **integration tests** to check the entire flow (API → DB).
- All tests pass (19 tests, 100% successful).

---

## Future improvements I am planning

- Add redirect feature (e.g., `GET /{shortCode}` should redirect to original URL).
- Add Docker Compose to run both app and database together easily.
- Write automated Postman test scripts (instead of only manual testing).
---

## Why I built this

- To learn how to structure a backend project properly.
- To practice database integration with Docker.
- To understand unit testing and integration testing in a real project.

---

## Note

This project is part of a coding challenge and also my learning journey in backend development.  
It's not for production use but demonstrates how I approach building and testing a REST API from scratch.

