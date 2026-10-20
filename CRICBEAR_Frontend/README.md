# CRICBEAR Frontend

Two React 18 apps (Create React App). Back to the [root README](../README.md) · [Backend README](../CRICBEAR_Backend/README.md).

| App | Path | Dev port | Served at | Audience |
|---|---|---|---|---|
| Public dashboard | `dashboardforpublicuser` | 3001 | `/` | Anyone, read-only |
| Admin and coach dashboard | `dashboardforadmincoach` | 3000 | `/admin` | Admins and coaches (sign-in required) |

`build.sh` builds both into one static site (`dist/`): the public app at the root and the admin app under `/admin` (it is built with `PUBLIC_URL=/admin`, and its router uses that as the basename).

## Design system

- **Palette:** ink `#14171e` for chrome, brand orange `#ff6f00` for actions and active states, paper `#f3f4f6` for the page, white cards with `#e3e5ea` borders, green for results and red for live.
- **Type:** Barlow Condensed for headings and scores, Barlow for text (Google Fonts).
- **Team badges:** circles in each franchise's colours with its short code, so no image assets are needed.
- **Logo and icons:** `brand/` holds the source logo (`logo-original.jpg`), a transparent mark, favicon and app icons; copies live in each app's `public/`.
- **Responsive:** one stylesheet per app. Under 860px the top navigation becomes a bottom tab bar, tables scroll horizontally, and under 520px team names shorten to their codes. Keyboard focus is visible and animations respect `prefers-reduced-motion`.
- The admin app keeps its original components and layers the brand on top with `src/theme.css`.

## Routes

Public: `/` home · `/matches` · `/tournaments` standings · `/teams` · `/playerprofile` players · `/live-match/:mid` match centre.
Admin (under `/admin`): `/login` · `/admin` dashboard · `/admin/TournamentManagement` · `/admin/ManageUser` · `/admin/ManageMatch` · `/coach` · `/coach/ManageTeam` · `/coach/CreatePlayer` · `/coach/RegisterTournament`.

## Run

```bash
cd dashboardforpublicuser && npm install && PORT=3001 npm start
cd dashboardforadmincoach && npm install && PORT=3000 npm start
npm test        # in either app
```

The backends must be running on `localhost:8083` (auth) and `localhost:8084` (tournament).

## Configuration

URLs in the source default to `http://localhost:8083` and `:8084`. `src/apiBase.js` (imported first in `index.js`) is an axios interceptor that swaps those prefixes at build time:

| Variable | Replaces |
|---|---|
| `REACT_APP_AUTH_URL` | `http://localhost:8083` |
| `REACT_APP_TOURNAMENT_URL` | `http://localhost:8084` |
| `REACT_APP_ADMIN_URL` | Target of the "Admin login" link in the public app (default `/admin`) |

## Layout

```
dashboardforpublicuser/src/
  index.js  apiBase.js  App.js (shell + routes)  index.css (design system)
  lib.js          API calls, hooks, team colours, formatting
  components.js   StandingsTable, MatchRow
  pages/          Home, Matches, Tournament, Teams, Players, MatchCentre
dashboardforadmincoach/src/
  components/     Login, AdminDashboard, ManageMatch, ManageTeam, ...
  styles.css      original styles      theme.css  brand layer
brand/            logo sources and generated icons
```
