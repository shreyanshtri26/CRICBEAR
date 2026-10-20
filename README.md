# CRICBEAR

<img src="CRICBEAR_Frontend/brand/icon-192.png" alt="CRICBEAR logo" width="96">

Open-source cricket tournament management. Admins create tournaments and generate schedules, coaches register teams and players, and the public follows fixtures, scorecards and standings.

**Live demo:** https://cricbear.vercel.app · **Admin and coach login:** https://cricbear.vercel.app/admin

| Part | Docs |
|---|---|
| Frontend (two React apps) | [CRICBEAR_Frontend/README.md](CRICBEAR_Frontend/README.md) |
| Backend (Spring Boot services, data model, API) | [CRICBEAR_Backend/README.md](CRICBEAR_Backend/README.md) |
| Notes for AI coding agents | [SKILL.md](SKILL.md) |

## Screenshots

![Home page with the next match, points table, latest results and fixtures](CRICBEAR_Frontend/docs/screenshots/home.jpg)
*Home: the next or live match, the points table, latest results and upcoming fixtures.*

![Match centre with the scoreboard and full batting and bowling scorecards](CRICBEAR_Frontend/docs/screenshots/match-centre.jpg)
*Match centre: scoreboard, result line, and batting and bowling scorecards for each innings.*

![Matches page filtered to upcoming fixtures, with search](CRICBEAR_Frontend/docs/screenshots/matches.jpg)
*Matches: live, upcoming and finished matches with search by team or stadium.*

![Group A and Group B standings tables with qualifying places marked](CRICBEAR_Frontend/docs/screenshots/standings.jpg)
*Standings: one table per group; the top two teams in each group are marked as qualifying.*

![Team cards and the selected team's squad](CRICBEAR_Frontend/docs/screenshots/teams.jpg)
*Teams: pick a franchise to see its squad, roles and overseas players.*

![Player leaderboard with role filters](CRICBEAR_Frontend/docs/screenshots/players.jpg)
*Players: leaderboard by runs or wickets, filterable by role.*

![Home, matches and match centre on a phone with a bottom tab bar](CRICBEAR_Frontend/docs/screenshots/mobile.jpg)
*Mobile: every page is responsive; navigation moves to a bottom tab bar on phones.*

![Admin and coach sign-in card](CRICBEAR_Frontend/docs/screenshots/admin-login.jpg)
*Admin and coach sign-in.*

![Admin dashboard with shortcuts to tournaments, matches and users](CRICBEAR_Frontend/docs/screenshots/admin-dashboard.jpg)
*Admin dashboard: shortcuts to tournaments, matches and user roles.*

## Architecture

```mermaid
flowchart LR
  subgraph Browser
    PUB["Public dashboard<br/>React 18 · route /"]
    ADM["Admin and coach dashboard<br/>React 18 · route /admin"]
  end
  subgraph Services["Spring Boot 3.3 services (Java 22)"]
    AUTH["userAuthorisation<br/>register · login · roles"]
    TOUR["tournament<br/>tournaments · teams · players<br/>matches · scoring · standings"]
    OPT["notificationService · gateway · eureka<br/>(optional, not used by the dashboards)"]
  end
  DB[("Postgres (Neon)")]
  PUB -->|REST/JSON| TOUR
  ADM -->|REST/JSON| AUTH
  ADM -->|REST/JSON| TOUR
  AUTH --> DB
  TOUR --> DB
```

- Both dashboards are static React builds served from one site: the public app at `/` and the admin and coach app under `/admin`.
- They call the auth and tournament services directly over REST. There is no server-side session; the admin app keeps the signed-in user in the browser.
- Each service is layered as `controller` → `service` → `repository` → `model` (Spring Data JPA on Postgres).
- The data model, enumerations, match lifecycle and REST API are documented in the [backend README](CRICBEAR_Backend/README.md); the design system, routes and configuration are in the [frontend README](CRICBEAR_Frontend/README.md).

## Repository layout

```
CRICBEAR/
├── CRICBEAR_Backend/    Spring Boot services + DB_SETUP/schema.sql
├── CRICBEAR_Frontend/   dashboardforpublicuser, dashboardforadmincoach, brand assets, docs/screenshots
├── SKILL.md             guide for AI coding agents
└── LICENSE              Apache-2.0
```

## Run locally

Prerequisites: JDK 22+, Maven, Node 18+, and a Postgres database (a free [Neon](https://neon.com) project works).

```bash
psql "$POSTGRES_URL" -f CRICBEAR_Backend/DB_SETUP/schema.sql

export DATABASE_URL='jdbc:postgresql://<host>/<db>?sslmode=require'
export DATABASE_USER='<user>' DATABASE_PASSWORD='<password>'
(cd CRICBEAR_Backend/userAuthorisation && mvn spring-boot:run)   # :8083
(cd CRICBEAR_Backend/tournament        && mvn spring-boot:run)   # :8084

(cd CRICBEAR_Frontend/dashboardforadmincoach && npm install && PORT=3000 npm start)
(cd CRICBEAR_Frontend/dashboardforpublicuser && npm install && PORT=3001 npm start)
```

## Known limitations

- Registration is open; promote the first admin in the database: `update users set role='ADMIN' where username='…';`
- `SecurityConfig` permits all requests, so authorization is enforced by the dashboards only. Harden the API before storing real data.

## Contributing

Issues and pull requests are welcome. Fork, branch, keep changes focused, and make sure `npm test` and `mvn test` pass. Contributions are released under [Apache-2.0](LICENSE).
