# CRICBEAR Backend

Spring Boot 3.3 (Java 22) microservices on Postgres. See the [root README](../README.md) for the overall architecture and data model.

| Service | Port | Purpose |
|---|---|---|
| `userAuthorisation` | 8083 | Registration, login, user roles (bcrypt passwords) |
| `tournament` | 8084 | Tournaments, teams, players, matches, scoring, standings |
| `notificationService` | 8081 | Scheduled reminders (optional) |
| `gateway` | 8080 | Spring Cloud Gateway (optional) |
| `eureka` | 8761 | Service registry (optional) |

`DB_SETUP/schema.sql` is the Postgres schema (idempotent `CREATE TABLE IF NOT EXISTS`).

## Run

```bash
psql "$POSTGRES_URL" -f DB_SETUP/schema.sql
export DATABASE_URL='jdbc:postgresql://<host>/<db>?sslmode=require'
export DATABASE_USER='<user>' DATABASE_PASSWORD='<password>'
cd userAuthorisation && mvn spring-boot:run     # :8083
cd tournament && mvn spring-boot:run            # :8084
```

Notes: with JDK 23+ the poms set `<proc>full</proc>` so Lombok's annotation processor runs. Tests: `mvn test`.

## Configuration

| Variable | Purpose |
|---|---|
| `DATABASE_URL` / `DATABASE_USER` / `DATABASE_PASSWORD` | Postgres JDBC connection (required) |
| `PORT` | HTTP port (defaults 8083 / 8084) |
| `CORS_ORIGINS` | Comma-separated allowed browser origins (default `http://localhost:3000,http://localhost:3001`) |

## REST API

### userAuthorisation (`/user`)
`GET /all` · `GET /{uid}` · `POST /register` · `POST /login` · `POST /getRole` · `PUT /setUserRole/{uid}`

### tournament
| Prefix | Endpoints |
|---|---|
| `/tournament` | `POST /create`, `PUT /update/{uid}`, `GET /getById/{id}`, `GET /getAll`, `DELETE /delete/{id}`, `POST /getByStatus`, `POST /start`, `GET /getByTid/{tid}` |
| `/team` | `POST /create`, `PUT /update`, `GET /getAll`, `GET /getByID/{id}`, `GET /getByName/{name}`, `GET /getTeamId/{coachid}`, `GET /findInDesc` (points table), `DELETE /delete/{id}` |
| `/player` | `POST /create`, `PUT /update`, `GET /getAll`, `DELETE /delete/{name}` |
| `/playerTeams` | `GET /all`, `GET /{tpid}`, `GET /team/{teamId}`, `GET /count/team/{id}`, `POST /addPlayerTeam`, `DELETE /delete/{pid}` |
| `/regTeam` | `POST /addRegTeam`, `GET /getAll`, `DELETE /delete` |
| `/match` | `GET /schedule/{tid}/{uid}`, `GET /start/{mid}`, `GET /update/{mid}`, `GET /semi/{tid}/{uid}`, `GET /final/{tid}/{uid}`, `GET /status/{status}`, `GET /getByTid/{tid}` |
| `/matchResult` | `GET /getResultById/{mid}` |
| `/innings`, `/ballByBall`, `/battingScore`, `/bowlingScore` | `GET /{mid}` |

## Layout

```
<service>/src/main/java/.../
  controller/   REST endpoints + CORS
  service/      business rules (scheduling, standings, scoring)
  repository/   Spring Data JPA repositories
  model/        JPA entities (enums stored as strings)
  config/       CORS / security
```
