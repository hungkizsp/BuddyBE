# BuddyEnglish Backend — AGENTS.md

## Quick start

```bash
# Build & run (first run creates schema + seeds data)
mvnw.cmd clean spring-boot:run

# Subsequent runs
mvnw.cmd spring-boot:run

# Run tests only
mvnw.cmd test
```

- Requires **Java 21+**, **Maven 3.9+**, **SQL Server** instance running on `localhost:1433`.
- Active profile is `dev` (set in `application.properties`). No `.env` file is needed — all config is in `application.properties`.
- VS Code launch config references `.env` file (optional, for IDE launch only).

## Authentication

- **JWT tokens stored in httpOnly cookies** (`access_token`, `refresh_token`), **not** `Authorization` header. When testing APIs that require auth, the cookie must be sent (via browser or HTTP client that handles cookies).
- Dev profile (`spring.profiles.active=dev`) permits **all requests** without authentication.
- Non-dev profiles require authentication on all endpoints except `POST /api/auth/login`, `POST /api/auth/register`, and Swagger UI paths.
- Access token expires in 15min, refresh token in 7 days.

## Architecture

**Entrypoint:** `com.exe.buddy_english_be.BuddyEnglishBeApplication` (`@EnableScheduling`)

**Package layout under `com.exe.buddy_english_be`:**
```
config/          — TestDataSeeder (CommandLineRunner, seeds on first run)
  seed/          — Individual seeders (RoleSeeder, UserSeeder, VocabularySeeder, etc.)
modules/         — 15 feature modules
security/        — SecurityConfig, JwtProvider, JwtAuthenticationFilter, CookieUtil
shared/          — BaseEntity (JPA mapped superclass), ApiResponse, exception classes
```

**Module convention** (each under `modules/<name>/`): `controller/`, `dto/`, `entity/`, `enums/`, `repository/`, `service/`

- All entities extend `BaseEntity` (provides `id`, `createdAt`, `updatedAt`, `createdBy`, `updatedBy` with lifecycle callbacks).
- Services use interface + `Impl` pattern (e.g. `AuthService` / `AuthServiceImpl`).

## Database & schema

- **SQL Server only** — no H2 or embedded DB for tests.
- Schema is managed entirely by `spring.jpa.hibernate.ddl-auto=update` — **no Flyway/Liquibase**.
- On first run, `TestDataSeeder` inserts seed data (checks for `testuser@buddy.com` to decide whether to run).
- Test account: `testuser@buddy.com` / `password123`

## API docs

- Swagger UI available at `/swagger-ui.html` and `/v3/api-docs`.

## Testing

- Only one test: `BuddyEnglishBeApplicationTests.contextLoads()` (verifies app context starts).

## Notable config

| Property | Value (dev) |
|---|---|
| `spring.jpa.hibernate.ddl-auto` | `update` |
| `spring.jpa.show-sql` | `true` |
| `app.cookie.secure` | `false` (dev), `true` (prod) |
| `app.cors.allowed-origin` | `http://localhost:5173` |
| `jwt.access-token.expiration-ms` | `900000` (15 min) |
| `jwt.refresh-token.expiration-ms` | `604800000` (7 days) |
| `gemini.model` | `gemini-2.5-flash` |

## Key dependencies

- **Spring Boot 4.x** (Jakarta EE), **Spring Security**, **Spring Data JPA**
- **Lombok** — annotation processing configured via `maven-compiler-plugin` (no separate Lombok plugin install needed for Maven builds)
- **jjwt 0.12.6** for JWT
- **SpringDoc OpenAPI 3.0.2**
- **MSSQL JDBC** driver
