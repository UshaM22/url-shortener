# URL Shortener

A secure URL shortening service built with Java and Spring Boot. Users can register, shorten long URLs into compact codes, share them, and see how many times 
each link has been clicked. Every user's links are private to them, protected by JWT authentication.

## Features

- **URL shortening** — converts a long URL into a short, unique code using Base62 encoding, with a collision-detection retry loop that guarantees no two URLs ever get the same code.
- **Redirection** — visiting a short link issues an HTTP 302 redirect to the original URL.
- **Click tracking** — every visit to a short link increments a click counter, so users can see how many times each of their links has been used.
- **Link expiry** — links can expire after a configurable number of days; expired links are rejected instead of redirecting.
- **User accounts** — register and log in with an email and password. Passwords are hashed with BCrypt before storage.
- **JWT authentication** — protected endpoints require a valid JSON Web Token, verified on every request by a custom security filter.
- **Per-user link ownership** — each short link is tied to the user who created it. Users can retrieve a list of only their own links.
- **Redis caching** — short-code lookups are cached in Redis to serve redirects quickly without hitting the database on every visit.
- **Input validation** — registration and shortening requests are validated (valid email format, minimum password length, non-empty valid URL) before processing.
- **Global exception handling** — errors return clean, consistent responses with appropriate HTTP status codes instead of raw stack traces.

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 4.1.0
- **Security:** Spring Security, JWT (jjwt), BCrypt
- **Data:** Spring Data JPA / Hibernate, MySQL
- **Caching:** Redis (via Spring Cache)
- **Validation:** Spring Boot Starter Validation
- **Testing:** JUnit 5, Mockito
- **Build:** Maven

## Architecture

The application follows a layered architecture:

- **Controller layer** — receives HTTP requests, validates input, and returns responses.
- **Service layer** — contains the business logic (code generation, redirect handling, authentication, link retrieval).
- **Repository layer** — talks to the database through Spring Data JPA.
- **Security layer** — a custom `OncePerRequestFilter` reads the JWT from the `Authorization` header, validates it, and sets the authenticated user in Spring's `SecurityContext`. `SecurityConfig` then decides which endpoints are public and which require authentication.

Data transfer objects (DTOs) separate the API's external shape from the internal database entities — for example, responses never expose the stored password hash, because the response DTO simply doesn't include it.

Short-code lookups are cached in Redis. Because a cached record holds a stale click count, the click counter is incremented with a direct database update query rather than read-modify-write, keeping the count accurate even on cache hits.

## API Endpoints

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| POST | `/api/register` | No | Register a new user |
| POST | `/api/login` | No | Log in and receive a JWT |
| POST | `/api/shorten` | Yes | Shorten a long URL |
| GET | `/{shortCode}` | No | Redirect to the original URL |
| GET | `/api/mylinks` | Yes | Retrieve the current user's links |

For protected endpoints, include the token in the request header:

```
Authorization: Bearer <your-token>
```

## Getting Started

### Prerequisites

- Java 17
- Maven
- MySQL running locally
- Redis running locally (e.g. via Docker: `docker run -d -p 6379:6379 redis`)

### Setup

1. Clone the repository:

   ```
   git clone https://github.com/UshaM22/url-shortener.git
   cd url-shortener
   ```

2. Create a MySQL database:

   ```sql
   CREATE DATABASE url_shortener_db;
   ```

3. Configure `src/main/resources/application.properties` with your database credentials and a JWT secret key (at least 32 characters):

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/url_shortener_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password

   app.base-url=http://localhost:8080
   app.default-expiry-days=30
   app.secret-key=your_32_plus_character_secret_key
   app.default-expiry-time=21600000

   spring.data.redis.host=localhost
   spring.data.redis.port=6379
   spring.cache.type=redis
   ```

4. Run the application:

   ```
   ./mvnw spring-boot:run
   ```

The API will be available at `http://localhost:8080`.

## Running Tests

```
./mvnw test
```

The project includes unit tests for the service layer covering registration, login (success and failure paths), and redirect behaviour (success and expiry), using JUnit 5 and Mockito.
