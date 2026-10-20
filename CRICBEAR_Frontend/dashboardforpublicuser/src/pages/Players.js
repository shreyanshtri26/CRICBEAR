import { useState } from 'react';
import { Badge, Empty, Loading, api, useLoad } from '../lib';

const ROLES = { BATSMAN: 'Batter', WICKETKEEPER: 'Keeper', ALLROUNDER: 'All-rounder', BOWLER: 'Bowler' };

// Squad rows carry only ids, so join in player and team names.
async function load() {
    const [rows, players, teams] = await Promise.all([api.squads(), api.players(), api.standings()]);
    const name = Object.fromEntries(players.map((p) => [p.pid, p.name]));
    const team = Object.fromEntries(teams.map((t) => [t.teamId, t.teamName]));
    return rows.map((r) => ({ ...r, name: name[r.pid], teamName: team[r.teamId] }));
}

export default function Players() {
    const { data, loading } = useLoad(load);
    const [role, setRole] = useState('');
    const [q, setQ] = useState('');
    const [sort, setSort] = useState('runsScored');
    if (loading) return <Loading />;
    const list = (data || [])
        .filter((p) => (!role || p.playerRole === role) && `${p.name} ${p.teamName}`.toLowerCase().includes(q.toLowerCase()))
        .sort((a, b) => b[sort] - a[sort]);
    return (
        <>
            <h1 className="page-title">Players</h1>
            <p className="page-sub">{data?.length || 0} players across the tournament, ranked by {sort === 'runsScored' ? 'runs' : 'wickets'}.</p>
            <div className="toolbar">
                <input className="search" type="search" placeholder="Search player or team" aria-label="Search players" value={q} onChange={(e) => setQ(e.target.value)} />
                <div className="seg" role="group" aria-label="Sort by">
                    <button aria-pressed={sort === 'runsScored'} onClick={() => setSort('runsScored')}>Runs</button>
                    <button aria-pressed={sort === 'wickets'} onClick={() => setSort('wickets')}>Wickets</button>
                </div>
            </div>
            <div className="chips" style={{ marginBottom: 16 }} role="group" aria-label="Filter by role">
                <button className="chip" aria-pressed={!role} onClick={() => setRole('')}>All</button>
                {Object.entries(ROLES).map(([k, label]) => <button key={k} className="chip" aria-pressed={role === k} onClick={() => setRole(k)}>{label}</button>)}
            </div>
            <section className="card">
                {list.length === 0 ? <Empty>No players match that search.</Empty> : (
                    <div className="table-wrap">
                        <table className="t">
                            <thead><tr><th>Player</th><th>Role</th><th>Runs</th><th>Balls</th><th>Wkts</th></tr></thead>
                            <tbody>
                                {list.map((p) => (
                                    <tr key={`${p.pid}-${p.teamId}`}>
                                        <td><span className="team-cell"><Badge name={p.teamName} size="sm" /><span>{p.name}{p.overseas && <span className="os">Overseas</span>}</span></span></td>
                                        <td><span className="role">{ROLES[p.playerRole]}</span></td>
                                        <td className="strong">{p.runsScored}</td><td>{p.balls}</td><td className="strong">{p.wickets}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </section>
        </>
    );
}
