# CRICBEAR 🏏

Open-source cricket tournament management. Admins create tournaments and generate schedules, coaches register teams and players, and the public follows matches, scores and standings.

License: [Apache-2.0](LICENSE) · Frontend docs: [CRICBEAR_Frontend](CRICBEAR_Frontend/README.md) · Backend docs: [CRICBEAR_Backend](CRICBEAR_Backend/README.md) · Agent guide: [SKILL.md](SKILL.md)

## Complete architecture

```mermaid
flowchart LR
  subgraph Clients
    PUB["Public dashboard<br/>React 18 + MUI · :3001"]
    ADM["Admin / Coach dashboard<br/>React 18 + MUI · :3000"]
  end
  subgraph Backend["Spring Boot 3.3 services (Java 22)"]
    AUTH["userAuthorisation :8083<br/>register · login · roles"]
    TOUR["tournament :8084<br/>tournaments · teams · players<br/>matches · scoring · standings"]
    NOTIF["notificationService :8081<br/>scheduled reminders (optional)"]
    GW["gateway :8080 (optional)"]
    EUR["eureka :8761 (optional)"]
  end
  DB[("Postgres (Neon)")]
  PUB -->|REST/JSON| TOUR
  ADM -->|REST/JSON| AUTH
  ADM -->|REST/JSON| TOUR
  AUTH --> DB
  TOUR --> DB
  NOTIF --> DB
  GW -.routes.-> AUTH
  GW -.registers.-> EUR
  NOTIF -.registers.-> EUR
```

| Component | Tech | Path | Port |
|---|---|---|---|
| Public dashboard | React 18, MUI, axios | `CRICBEAR_Frontend/dashboardforpublicuser` | 3001 |
| Admin/Coach dashboard | React 18, MUI, axios | `CRICBEAR_Frontend/dashboardforadmincoach` | 3000 |
| Auth service | Spring Boot, Spring Security, JPA | `CRICBEAR_Backend/userAuthorisation` | 8083 |
| Tournament service | Spring Boot, JPA | `CRICBEAR_Backend/tournament` | 8084 |
| Notification service | Spring Boot, JPA (optional) | `CRICBEAR_Backend/notificationService` | 8081 |
| Gateway / Eureka | Spring Cloud (optional) | `CRICBEAR_Backend/gateway`, `eureka` | 8080 / 8761 |
| Database | Postgres | `CRICBEAR_Backend/DB_SETUP/schema.sql` | 5432 |

**Request flow.** The dashboards call the auth and tournament services directly over REST; there is no server-side session. Login returns the user (and role), which the admin/coach dashboard keeps client-side to show role-specific screens. Eureka and the gateway are optional local conveniences and are not used by the dashboards.

**Layering (each service).** `controller` (REST, CORS) → `service` (business rules: scheduling, standings, role checks) → `repository` (Spring Data JPA) → `model` (JPA entities). Enums are stored as strings.

**Match lifecycle.** `UPCOMING` → admin starts the tournament (`POST /tournament/start`) → teams are split into groups and `/match/schedule/{tid}/{uid}` creates `NORMAL` matches → `/match/start/{mid}` sets `LIVE`, creates innings and ball-by-ball/batting/bowling scores → on completion a `match_result` is written and team points/NRR update → `/match/semi/{tid}/{uid}` and `/match/final/{tid}/{uid}` create `SEMIFINAL`/`FINAL` matches from the points table → tournament `COMPLETED`.

## Data model

```mermaid
erDiagram
  USERS ||--o{ TOURNAMENT : creates
  USERS ||--o{ TEAM : "coaches"
  USERS ||--o{ NOTIFICATIONS : receives
  TOURNAMENT ||--o{ REG_TEAM : has
  TEAM ||--o{ REG_TEAM : "registers in"
  TOURNAMENT ||--o{ MATCHES : schedules
  TEAM ||--o{ MATCHES : "plays (teamid_1, teamid_2)"
  MATCHES ||--o{ INNINGS : has
  MATCHES ||--o| MATCH_RESULT : "decided by"
  TEAM ||--o{ MATCH_RESULT : wins
  INNINGS ||--o{ BALLBYBALL : records
  INNINGS ||--o{ BATTING_SCORE : has
  INNINGS ||--o{ BOWLING_SCORE : has
  TEAM ||--o{ TEAM_PLAYER : fields
  PLAYER_PROFILE ||--o{ TEAM_PLAYER : "appears as"

  USERS { int uid PK "username unique, password bcrypt, first_name, last_name, email, role ADMIN|COACH" }
  TOURNAMENT { int tid PK "t_name unique, start_date, end_date, status, uid FK" }
  TEAM { int teamid PK "team_name unique, coachid FK, matches_played/won/lost/drawn/abandoned, points, nrr" }
  REG_TEAM { int rtid PK "tid FK, teamid FK, group_number" }
  MATCHES { int mid PK "teamid_1 FK, teamid_2 FK, match_date, status, stadium, tid FK, match_type" }
  INNINGS { int iid PK "mid FK, batting_id FK, bowling_id FK, runs, wickets, overs" }
  BALLBYBALL { int bid PK "iid FK, over_number, ball_number, batsmen1, batsmen2, bowler, runs_scored, extra, wicket, total, wicket_number" }
  BATTING_SCORE { int bsid PK "iid FK, player_name, runs_scored, balls_faced, fours, six, is_out" }
  BOWLING_SCORE { int bsid PK "iid FK, player_name, runs_given, overs, wickets" }
  MATCH_RESULT { int mrid PK "mid FK, toss_desicion, toss_win_team, winner_id FK" }
  PLAYER_PROFILE { int pid PK "name, overseas, flag, player_roles" }
  TEAM_PLAYER { int tpid PK "pid FK, teamid FK, overseas, player_roles, runs_scored, balls, wickets, overs, runs_given, flag" }
  NOTIFICATIONS { int nid PK "uid FK, reminder_type, message, sent_at" }
```

Enumerations (stored as `VARCHAR`):

| Field | Values |
|---|---|
| `users.role` | `ADMIN`, `COACH` |
| `tournament.status`, `matches.status` | `LIVE`, `UPCOMING`, `COMPLETED` |
| `matches.match_type` | `NORMAL`, `SEMIFINAL`, `FINAL` |
| `ballbyball.extra` | `WIDE`, `NOBALL` |
| `ballbyball.wicket` | `CAUGHT`, `BOWLED`, `RUNOUT`, `LBW` |
| `match_result.toss_desicion` | `BAT`, `BOWL` |
| `*.player_roles` | `BATSMAN`, `BOWLER`, `WICKETKEEPER`, `ALLROUNDER` |

Delete rules: removing a user cascades to their tournaments, teams and notifications; removing a tournament sets `matches.tid` to null; removing a team cascades to its registrations, matches, innings and players. The column name `toss_desicion` (sic) matches the `MatchResult` entity.

## Roles

- **Admin:** create tournaments, manage users and roles, start a tournament (generates the schedule), start matches, run semi-finals and final.
- **Coach:** register, create/edit a team and players, register the team for a tournament.
- **Public:** read-only view of tournaments, teams, players, matches and standings.

## Run locally

Prerequisites: JDK 22+, Maven, Node 18+, a Postgres database (a free [Neon](https://neon.com) project works).

```bash
psql "$POSTGRES_URL" -f CRICBEAR_Backend/DB_SETUP/schema.sql

export DATABASE_URL='jdbc:postgresql://<host>/<db>?sslmode=require'
export DATABASE_USER='<user>' DATABASE_PASSWORD='<password>'
(cd CRICBEAR_Backend/userAuthorisation && mvn spring-boot:run)   # :8083
(cd CRICBEAR_Backend/tournament        && mvn spring-boot:run)   # :8084

(cd CRICBEAR_Frontend/dashboardforadmincoach && npm install && PORT=3000 npm start)
(cd CRICBEAR_Frontend/dashboardforpublicuser && npm install && PORT=3001 npm start)
```

Details and configuration variables are in the [backend](CRICBEAR_Backend/README.md) and [frontend](CRICBEAR_Frontend/README.md) READMEs.

## Known limitations

- Registration is open; promote the first admin in the database: `update users set role='ADMIN' where username='…';`
- `SecurityConfig` permits all requests, so authorization is enforced by the dashboards only. Harden before storing real data.

## Contributing

Issues and pull requests are welcome. Fork, branch, keep changes focused, and make sure `npm test` / `mvn test` pass. Contributions are released under Apache-2.0.
