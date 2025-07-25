# URL Shortener Backend

A simple URL Shortener backend service built with **Spring Boot (Kotlin)**, **PostgreSQL**, and **Docker**.

---

## Overview

This project is part of a coding challenge. It provides an API to:
- Shorten long URLs into short codes.
- Redirect short codes back to the original URLs.
- Includes a basic `/health` endpoint for quick status checks.

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
 │   │   └── HealthController.kt             # Health check endpoint
 │   └── resources/
 │       ├── application.yml                 # Database configuration
 │       └── application.properties          # (Generated default, unused)
 └── test/
     └── kotlin/com/dkb/urlshortener/
```

---

## Running the Project

### 1. Start PostgreSQL using Docker

```bash
cd dkb-postgres-docker
docker-compose up -d
```

This will start a PostgreSQL container accessible at `localhost:5432` with:
- **Database**: `urlshortener`
- **User**: `postgres`
- **Password**: `password`

---

### 2. Run the Spring Boot Application

From the project root (`urlshortener`):

```bash
./gradlew bootRun
```

---

### 3. Test the Health Endpoint

Open browser or use curl:

```
http://localhost:8080/health
```

Expected response:
```
Application is running!
```

---

## Future Endpoints (Planned)

- `POST /shorten` – Create a shortened URL
- `GET /{shortCode}` – Redirect to original URL
- `GET /urls` – (Optional) List all shortened URLs

---

## License

This project is for coding challenge purposes only.
