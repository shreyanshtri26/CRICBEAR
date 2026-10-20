# CRICBEAR Frontend

Two React 18 + MUI apps (Create React App). See the [root README](../README.md) for the overall architecture.

| App | Path | Port | Audience |
|---|---|---|---|
| Admin / Coach dashboard | `dashboardforadmincoach` | 3000 | Admins and coaches (login required) |
| Public dashboard | `dashboardforpublicuser` | 3001 | Anyone (read-only) |

## Run

```bash
cd dashboardforadmincoach && npm install && PORT=3000 npm start
cd dashboardforpublicuser && npm install && PORT=3001 npm start
npm test        # in either app
```

Backends must be running on `localhost:8083` (auth) and `localhost:8084` (tournament).

## Configuration

URLs in the source default to `http://localhost:8083` / `:8084`. `src/apiBase.js` (imported first in `index.js`) is an axios interceptor that swaps those prefixes at build time:

| Variable | Replaces |
|---|---|
| `REACT_APP_AUTH_URL` | `http://localhost:8083` |
| `REACT_APP_TOURNAMENT_URL` | `http://localhost:8084` |

## Features

**Admin:** log in, create tournaments, manage users and roles, start a tournament (generates the schedule), start/update matches, run semi-finals and final.
**Coach:** register, log in, create/edit a team and players, register for a tournament.
**Public:** view tournaments, teams, players, matches, scores and standings.

## Layout

```
<app>/src/
  index.js        entry (imports apiBase.js first)
  apiBase.js      backend URL rewrite
  App.js          routes
  components/     screens (Login, ManageTeam, ManageMatch, CreatePlayer, …)
  ApiService.js   REST calls (public dashboard)
```
