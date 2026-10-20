import { useEffect, useState } from 'react';
import axios from 'axios';

// Dev defaults; apiBase.js swaps the host at build time.
const T = 'http://localhost:8084';
const get = (path) => axios.get(`${T}${path}`).then((r) => r.data);

export const api = {
    tournaments: () => get('/tournament/getAll'),
    standings: () => get('/team/findInDesc'),
    groups: () => get('/regTeam/getAll'),
    matches: (tid) => get(`/match/getByTid/${tid}`),
    results: () => get('/matchResult/all'),
    result: (mid) => get(`/matchResult/getResultById/${mid}`),
    balls: (mid) => get(`/ballByBall/${mid}`),
    batting: (mid) => get(`/battingScore/${mid}`),
    bowling: (mid) => get(`/bowlingScore/${mid}`),
    teams: () => get('/team/getAll'),
    squad: (teamId) => get(`/playerTeams/team/${teamId}`),
    squads: () => get('/playerTeams/all'),
    players: () => get('/player/getAll'),
};

// Loads data once; `deps` re-runs it. Exposes { data, error, loading }.
export function useLoad(fn, deps = []) {
    const [state, set] = useState({ data: null, error: null, loading: true });
    useEffect(() => {
        let live = true;
        set((s) => ({ ...s, loading: true }));
        fn().then((data) => live && set({ data, error: null, loading: false }),
            (error) => live && set({ data: null, error, loading: false }));
        return () => { live = false; };
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, deps);
    return state;
}

// IPL franchise colours: [background, foreground]
const COLORS = {
    'Chennai Super Kings': ['#f9cd05', '#14171e', 'CSK'],
    'Mumbai Indians': ['#004ba0', '#fff', 'MI'],
    'Royal Challengers Bengaluru': ['#d1171b', '#fff', 'RCB'],
    'Kolkata Knight Riders': ['#3a225d', '#f2c14e', 'KKR'],
    'Sunrisers Hyderabad': ['#ff822a', '#14171e', 'SRH'],
    'Delhi Capitals': ['#17449b', '#fff', 'DC'],
    'Punjab Kings': ['#d71920', '#fff', 'PBKS'],
    'Rajasthan Royals': ['#ea1a85', '#fff', 'RR'],
    'Gujarat Titans': ['#1c2541', '#c9a64b', 'GT'],
    'Lucknow Super Giants': ['#0c8ce9', '#fff', 'LSG'],
};

export const short = (name = '') =>
    (COLORS[name] || [0, 0, name.split(' ').map((w) => w[0]).join('').slice(0, 3).toUpperCase()])[2];

export function Badge({ name, size = '' }) {
    const [bg, fg] = COLORS[name] || ['#2c3340', '#fff'];
    return <span className={`badge ${size}`} style={{ background: bg, color: fg }} aria-hidden="true">{short(name)}</span>;
}

export const fmtDate = (d, opts = { day: 'numeric', month: 'short' }) =>
    new Date(d).toLocaleDateString('en-GB', opts);

export const isToday = (d) => new Date(d).toDateString() === new Date().toDateString();

export function resultLine(r) {
    if (!r) return '';
    const first = r.runsScoredInFirstinnings;
    const second = r.runsScoredInSecondinnings;
    if (first === second) return 'Match tied';
    if (r.matchWinner === r.battingFirst) return `${short(r.matchWinner)} won by ${first - second} runs`;
    const left = 10 - r.wicketsInSecondinnings;
    return `${short(r.matchWinner)} won by ${left} wicket${left === 1 ? '' : 's'}`;
}

export const oversText = (balls) => `${Math.floor(balls / 6)}.${balls % 6}`;

export function Loading() {
    return <div className="card" style={{ padding: 16, display: 'grid', gap: 12 }} role="status" aria-label="Loading">
        {[70, 90, 55].map((w) => <div key={w} className="skeleton" style={{ width: `${w}%` }} />)}
    </div>;
}

export const Empty = ({ children }) => <div className="empty">{children}</div>;

// The tournament to show: the live one, else the first. Plus its matches (oldest first).
export async function loadSeason() {
    const ts = await api.tournaments();
    const t = ts.find((x) => x.status === 'LIVE') || ts[0];
    const matches = t ? (await api.matches(t.tid)).sort((a, b) => a.mid - b.mid) : [];
    return { t, matches };
}

// Results of finished matches, keyed by mid.
export async function loadResults() {
    const rs = await api.results();
    return Object.fromEntries(rs.map((r) => [r.mid, r]));
}
