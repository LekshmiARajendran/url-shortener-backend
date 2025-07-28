# URL Shortener Backend

A simple URL Shortener backend service built with **Spring Boot (Kotlin)**, **PostgreSQL**, and **Docker**.

---

## Overview

This project does the following:

- Shortens a long URL into a short code.
- Redirects a short code back to the original URL.
- Provides a `/health` endpoint to check if the application is running.

**Note:** Endpoints are now fully tested with Postman for both POST (shorten URL) and GET (retrieve original URL).

---

## Tech Stack

- **Kotlin** – Backend language
- **Spring Boot** – Web framework
- **Spring Data JPA** – Database ORM
- **PostgreSQL** – Database
- **Docker** – Containerized PostgreSQL setup
- **Gradle (Kotlin DSL)** – Build tool

---

## Project Structure

```
src/
 ├── main/
 │   ├── kotlin/com/dkb/urlshortener/
 │   │   ├── UrlshortenerApplication.kt      # Main Spring Boot entry point
 │   │   ├── HealthController.kt             # Health check endpoint
 │   │   ├── controller/                     # Controller layer (API endpoints)
 │   │   ├── service/                        # Service layer (business logic)
 │   │   ├── repository/                     # Repository layer (database access)
 │   │   └── model/                          # Entity classes
 │   └── resources/
 │       ├── application.yml                 # Database configuration
 └── test/
     └── kotlin/com/dkb/urlshortener/
```

---

## Running the Project

### 1. Start PostgreSQL using Docker

```bash
docker start urlshortener-db
```

If container does not exist, create it with:

```bash
docker run --name urlshortener-db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -e POSTGRES_DB=urlshortener -p 5432:5432 -d postgres:16
```

---

### 2. Run the Spring Boot Application

From the project root (`urlshortener`):

```bash
./gradlew bootRun
```

---

### 3. Test the Endpoints

#### Health Check

```
GET http://localhost:8080/health
```

Response:
```
Application is running!
```

#### Shorten URL

```
POST http://localhost:8080/shorten
Body (JSON):
{
  "longUrl": "https://example.com"
}
```

Response:
```
{
  "shortCode": "abc123"
}
```

#### Redirect Short Code

```
GET http://localhost:8080/{shortCode}
```

Redirects to the original URL.

---

## Future Work

- Implement endpoint to list all stored URLs (`GET /urls` – optional).
- Add unit tests, integration tests, and acceptance tests.
- Improve validation and error handling for edge cases.

---

## License

This project is for coding challenge purposes only.
