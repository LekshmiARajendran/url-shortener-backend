# URL Shortener Backend

A backend URL shortening service built using Kotlin, Spring Boot, PostgreSQL, and Docker.
This project was created as part of a coding challenge to demonstrate backend development skills, clean architecture, and integration testing with real databases.
---

# Features

- Convert long URLs into random 6-character short codes using salted hashing (MD5 + Base64).
- Always generates a new short code even for the same URL (no reuse).
- Retrieve the original URL from its short code.
- URL format validation before shortening.
- No endpoint for listing all URLs (avoids exposing sensitive data).
- Clean architecture: Controller → Service → Repository → DB.
- Unit, repository, and integration tests included.

---

## Technologies I used

- **Kotlin** – Programming language
- **Spring Boot** – REST API framework
- **Spring Data JPA** – For database interactions
- **PostgreSQL** – Database
- **Docker** – To run PostgreSQL in a container
- **JUnit 5 + MockK + SpringMockK** – Testing framework
- **Gradle (Kotlin DSL)** – Build tool
---

## How the project is organized

- `controller/` → Handles API requests (endpoints)
- `service/` → Contains business logic
- `repository/` → Connects to the database
- `model/` → Database entity
- `dto/` → Request and response objects
- `exception/` → Reserved for future custom error handling
- `test/` →  Unit, repository, and integration tests

---

## Setup & Run

1. **Start PostgreSQL via Docker**
   `docker run --name urlshortener-db \
   -e POSTGRES_USER=postgres \
   -e POSTGRES_PASSWORD=password \
   -e POSTGRES_DB=urlshortener \
   -p 5432:5432 -d postgres:16`

   **Test DB (Integration tests)**
   `docker run --name urlshortener-test-db \
   -e POSTGRES_USER=postgres \
   -e POSTGRES_PASSWORD=postgres \
   -e POSTGRES_DB=urlshortener_test \
   -p 5433:5432 -d postgres:16`

2. **Run the application**
    - Open the project in IntelliJ and run `UrlshortenerApplication.kt`, 
    - **or** use terminal:  
      `./gradlew bootRun`

3. **Test the API endpoints using Postman**
    - Shorten URL: `POST http://localhost:8080/api/shorten`  
      Body: `{ "url": "https://example.com" }`
    - Retrieve original: `GET http://localhost:8080/api/original/{shortCode}`

---

## How I tested it

- **Unit tests** for controller and service (MockK + SpringMockK)
- **Repository tests** using PostgreSQL test DB
- **Integration tests** validating full flow
- Current: 9 passing test cases (future: expand to cover invalid inputs & edge cases)

## Run all tests:
`./gradlew test`
---

# DB Reset (if needed)

- If testing repeatedly, clear tables with:
- `docker exec -it urlshortener-db psql -U postgres -d urlshortener -c "TRUNCATE url_mapping RESTART IDENTITY CASCADE;`

## Future improvements I am planning

- Use Docker Compose to spin up app + DB together.
- Add custom exceptions + global error handler (clean JSON error responses).
- Write automated Postman test scripts (instead of only manual testing).
- Add redirect feature (e.g., `GET /{shortCode}` should redirect to original URL).
- Expand test coverage (invalid URL, collision edge cases).
---
## Why I built this

- To learn how to structure a backend project properly.
- To practice database integration with Docker.
- To understand unit testing and integration testing in a real project.


