# URL Shortener Backend

- A backend URL shortening service built using Kotlin, Spring Boot, PostgreSQL, and Docker.
- This project was created as part of a coding challenge to demonstrate backend development skills, clean architecture, and integration testing with real databases.
---

# Features

- Convert long URLs into random 6-character short codes using salted hashing (MD5 + Base64).

- uplicate URL handling: returns the same short code if the URL is already shortened (avoids duplicates).

- Retrieve the original URL from its short code.

- URL format validation before shortening (invalid URLs return 400 Invalid URL format).

- Custom error handling: invalid short codes return 404 with JSON message ({ "error": "no url found for {shortCode}" }).

- Clean architecture: Controller → Service → Repository → DB.

- Unit, repository, and integration tests included.

- Stricter URL validation: now only `http` and `https` protocols are allowed.

- 405 Method Not Allowed handling for unsupported HTTP methods.

- Unified 400 Bad Request handling for malformed JSON bodies.


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
- `exception/` → Custom error handling (UrlNotFoundException, GlobalExceptionHandler)
- `test/` →  Unit, repository, and integration tests

---

## Setup & Run

1. **Start PostgreSQL via Docker**
   `docker run --name urlshortener-db    -e POSTGRES_USER=postgres    -e POSTGRES_PASSWORD=password    -e POSTGRES_DB=urlshortener    -p 5432:5432 -d postgres:16`

   **Test DB (Integration tests)**
   `docker run --name urlshortener-test-db    -e POSTGRES_USER=postgres    -e POSTGRES_PASSWORD=postgres    -e POSTGRES_DB=urlshortener_test    -p 5433:5432 -d postgres:16`

2. **Run the application**
   - Open the project in IntelliJ and run `UrlshortenerApplication.kt`,
   - **or** use terminal:  
     `./gradlew bootRun`

   3. **Test the API endpoints using Postman**

##### Shorten URL:

         POST http://localhost:8080/api/shorten
         Body: { "originalUrl": "https://example.com" }

##### Retrieve original URL:

        GET http://localhost:8080/api/{shortCode}

#####    Invalid short code example:

        GET http://localhost:8080/api/invalid → 404 { "error": "no url found for invalid" }
---

## How I tested it

- **Unit tests** for controller and service (MockK + SpringMockK)

- **Repository tests** using PostgreSQL test DB

- **Integration tests** validating full flow (POST + GET + errors)

- Current: 21 passing test cases (covering valid, duplicate, invalid scenarios, malformed body, and wrong HTTP method)

## Run all tests:

`./gradlew test`
---

# DB Reset (if needed)

- If testing repeatedly, clear tables with:
- `docker exec -it urlshortener-db psql -U postgres -d urlshortener -c "TRUNCATE url_mapping RESTART IDENTITY CASCADE;`

## Future improvements I am planning

- Use Docker Compose to spin up app + DB together.

- Add redirect feature (e.g., GET /{shortCode} redirects to original URL).

- Write automated Postman test scripts.

- Expand test coverage for edge cases (collisions, malformed inputs).

---
## Why I built this

- To learn how to structure a backend project properly.

- To practice database integration with Docker.

- To understand unit testing and integration testing in a real project.

---
## Testing Summary

- **Controller Tests**: Verify positive cases (201 Created for valid POST, 200 for valid GET) and negative/edge cases (400 for invalid or malformed URLs, 404 for invalid short codes, 405 for wrong HTTP methods).
- **Service Tests**: Validate business logic including duplicates, strict http/https validation, and error handling for invalid inputs.
- **Repository Tests**: Ensure correct persistence and retrieval by original URL and short code.
- **Integration Tests**: End-to-end flow with real PostgreSQL test DB, covering valid, duplicate, invalid, and malformed input scenarios. Database cleanup runs before each test.

(Current: 21 passing test cases — positive, negative, and edge cases covered)
---

## Note
I added these changes after submitting the coding challenge, after self-reviewing through the codes.
