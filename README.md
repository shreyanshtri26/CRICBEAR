# CRICBEAR 🏏

Open-source cricket tournament management: admins create tournaments and generate schedules, coaches register teams and players, and the public follows live matches, scores and standings.

- **Public dashboard:** https://cricbear.vercel.app
- **Admin / coach dashboard:** https://cricbear-admin.vercel.app
- **License:** [Apache-2.0](LICENSE)

## Architecture

```
                 ┌──────────────────────────┐   ┌──────────────────────────┐
   Browser ────► │ Public dashboard (React) │   │ Admin/Coach dash (React) │
                 │ Vercel · :3001 locally   │   │ Vercel · :3000 locally   │
                 └────────────┬─────────────┘   └───────┬──────────┬───────┘
                              │ REST/JSON (axios)       │          │
                              ▼                         ▼          ▼
                 ┌──────────────────────────┐   ┌──────────────────────────┐
                 │ tournament service       │   │ userAuthorisation service│
                 │ Spring Boot · :8084      │   │ Spring Boot · :8083      │
                 │ teams, players, matches, │   │ register, login, roles   │
                 │ scoring, standings       │   │ (bcrypt passwords)       │
                 └────────────┬─────────────┘   └────────────┬─────────────┘
                              └───────────────┬──────────────┘
                                              ▼
                                 ┌──────────────────────────┐
                                 │ Neon serverless Postgres │
                                 │ (JPA / Hibernate)        │
                                 └──────────────────────────┘
```

| Part | Tech | Path | Port |
|---|---|---|---|
| Public dashboard | React 18, MUI, axios | `CRICBEAR_Frontend/dashboardforpublicuser` | 3001 |
| Admin/Coach dashboard | React 18, MUI, axios | `CRICBEAR_Frontend/dashboardforadmincoach` | 3000 |
| Auth service | Spring Boot 3.3, Spring Security, JPA | `CRICBEAR_Backend/userAuthorisation` | 8083 |
| Tournament service | Spring Boot 3.3, JPA | `CRICBEAR_Backend/tournament` | 8084 |
| Notification service | Spring Boot, JPA | `CRICBEAR_Backend/notificationService` | 8081 (optional, not deployed) |
| Eureka / Gateway | Spring Cloud | `CRICBEAR_Backend/eureka`, `gateway` | 8761 / 8080 (optional, not used by the frontends) |
| Database | Neon Postgres | schema: `CRICBEAR_Backend/DB_SETUP/schema.sql` | – |

The frontends call the auth and tournament services directly. Eureka and the gateway are only needed if you want service discovery locally.

### Data model

`users` → `tournament` → `reg_team` ← `team` ← `team_player` → `player_profile`;
`matches` (per tournament, NORMAL / SEMIFINAL / FINAL) → `innings` → `ballbyball`, `batting_score`, `bowling_score`; `match_result`; `notifications`.
Enum columns (roles, status, match type, …) are stored as `VARCHAR` and mapped with `@Enumerated(EnumType.STRING)`.

### Roles

- **Admin:** create tournaments, manage users and roles, start a tournament (generates the schedule), start matches, run semi-finals and final.
- **Coach:** register, create/edit a team and players, register for a tournament.
- **Public:** read-only view of tournaments, teams, players, matches and standings.

## Run locally

Prerequisites: JDK 22+, Maven, Node 18+, and a Postgres database (a free [Neon](https://neon.com) project works well).

```bash
# 1. Database: create a Neon project, then apply the schema
psql "$NEON_CONNECTION_STRING" -f CRICBEAR_Backend/DB_SETUP/schema.sql

# 2. Backends (JDBC URL, not the postgresql:// URL)
export DATABASE_URL='jdbc:postgresql://<host>/<db>?sslmode=require'
export DATABASE_USER='<user>'
export DATABASE_PASSWORD='<password>'
(cd CRICBEAR_Backend/userAuthorisation && mvn spring-boot:run)   # :8083
(cd CRICBEAR_Backend/tournament        && mvn spring-boot:run)   # :8084

# 3. Frontends (separate terminals)
(cd CRICBEAR_Frontend/dashboardforadmincoach && npm install && PORT=3000 npm start)
(cd CRICBEAR_Frontend/dashboardforpublicuser && npm install && PORT=3001 npm start)
```

Tests: `npm test` in each dashboard, `mvn test` in each service.

### Configuration

| Variable | Used by | Purpose |
|---|---|---|
| `DATABASE_URL`, `DATABASE_USER`, `DATABASE_PASSWORD` | backends | Postgres JDBC connection |
| `PORT` | backends | HTTP port (defaults 8083 / 8084) |
| `CORS_ORIGINS` | backends | Comma-separated allowed browser origins (default: localhost 3000/3001) |
| `REACT_APP_AUTH_URL` | frontends (build time) | Base URL of the auth service, replaces `http://localhost:8083` |
| `REACT_APP_TOURNAMENT_URL` | frontends (build time) | Base URL of the tournament service, replaces `http://localhost:8084` |

## Deploy for free

| Layer | Host | Notes |
|---|---|---|
| Database | Neon (free plan) | scales to zero, `aws-us-east-2` |
| Backends | Render free web services (Docker) | `render.yaml` blueprint; sleep after ~15 min idle, so the first request is slow |
| Frontends | Vercel Hobby | two projects, one per dashboard |

1. **Database:** create a Neon project and run `schema.sql` (see above).
2. **Backends:** in Render choose *New → Blueprint*, select this repo, and fill in `DATABASE_URL`, `DATABASE_USER`, `DATABASE_PASSWORD`. This creates `cricbear-auth` and `cricbear-tournament`.
3. **Frontends:** from each dashboard directory run
   ```bash
   vercel link --project <cricbear | cricbear-admin>
   vercel env add REACT_APP_AUTH_URL production        # https://cricbear-auth.onrender.com
   vercel env add REACT_APP_TOURNAMENT_URL production  # https://cricbear-tournament.onrender.com
   vercel --prod
   ```
4. Make sure `CORS_ORIGINS` on the backends lists both Vercel URLs.

## Known limitations

- Registration is open and the first admin must be promoted directly in the database (`update users set role='ADMIN' where username='…'`).
- `SecurityConfig` permits all requests; authorization is enforced by the dashboards only. Harden before storing real data.
- `notificationService`, `eureka` and `gateway` are not part of the free deployment.

## Contributing

Issues and pull requests are welcome. Fork, create a branch, keep changes focused, and make sure `npm test` / `mvn test` pass. By contributing you agree your work is released under Apache-2.0.
