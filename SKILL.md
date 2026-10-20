---
name: cricbear
description: Work on the CRICBEAR cricket tournament app (React dashboards + Spring Boot services on Postgres). Use when adding features, fixing bugs, running, or testing this repo.
---

# CRICBEAR

Cricket tournament manager: React admin/coach and public dashboards, Spring Boot `userAuthorisation` (:8083) and `tournament` (:8084) services, Postgres. Full architecture and ER diagram: `README.md`. Per-part docs: `CRICBEAR_Backend/README.md`, `CRICBEAR_Frontend/README.md`.

## Map

- Schema: `CRICBEAR_Backend/DB_SETUP/schema.sql` (must match the JPA entities; enums are `VARCHAR`, users table is `users`)
- Entities: `CRICBEAR_Backend/<service>/src/main/java/.../model`
- Business rules (scheduling, standings): `.../service`
- Screens: `CRICBEAR_Frontend/<app>/src/components`; public API calls in `dashboardforpublicuser/src/ApiService.js`

## Run

```bash
export DATABASE_URL='jdbc:postgresql://<host>/<db>?sslmode=require' DATABASE_USER=… DATABASE_PASSWORD=…
(cd CRICBEAR_Backend/userAuthorisation && mvn spring-boot:run)
(cd CRICBEAR_Backend/tournament && mvn spring-boot:run)
(cd CRICBEAR_Frontend/dashboardforadmincoach && PORT=3000 npm start)
(cd CRICBEAR_Frontend/dashboardforpublicuser && PORT=3001 npm start)
```

Tests: `mvn test` per service, `npm test` per app.

## Conventions and gotchas

- Changing an entity means changing `schema.sql` too; there is no `ddl-auto`.
- Keep enum values in sync across entity, schema and frontend (see the enum table in `README.md`).
- Column `match_result.toss_desicion` is misspelled on purpose to match the entity.
- Frontend URLs stay as `http://localhost:808x`; `src/apiBase.js` rewrites them via `REACT_APP_AUTH_URL` / `REACT_APP_TOURNAMENT_URL`.
- Never commit `.env` or credentials; DB config comes only from environment variables.
- JDK 23+ needs `<proc>full</proc>` in the pom compiler config for Lombok (already set).
- Auth is permissive (`SecurityConfig` permits all); do not rely on it for protection.
