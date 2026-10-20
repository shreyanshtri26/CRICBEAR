import { useState } from 'react';
import { Badge, Empty, Loading, api, useLoad } from '../lib';

const ROLES = { BATSMAN: 'Batter', BOWLER: 'Bowler', ALLROUNDER: 'All-rounder', WICKETKEEPER: 'Keeper' };
const ORDER = ['BATSMAN', 'WICKETKEEPER', 'ALLROUNDER', 'BOWLER'];

function Squad({ team }) {
    const { data, loading } = useLoad(() => api.squad(team.teamId), [team.teamId]);
    if (loading) return <Loading />;
    const players = [...(data || [])].sort((a, b) => ORDER.indexOf(a.playerRole) - ORDER.indexOf(b.playerRole));
    return (
        <section className="card" aria-label={`${team.teamName} squad`}>
            <div className="card-head"><h2>{team.teamName} squad</h2></div>
            {players.length === 0 ? <Empty>No players registered yet.</Empty> : (
                <div className="table-wrap">
                    <table className="t">
                        <thead><tr><th>Player</th><th>Role</th><th>Runs</th><th>Wkts</th></tr></thead>
                        <tbody>
                            {players.map((p) => (
                                <tr key={p.pid}>
                                    <td><strong>{p.name}</strong>{p.overseas && <span className="os">Overseas</span>}</td>
                                    <td><span className="role">{ROLES[p.playerRole]}</span></td>
                                    <td>{p.runsScored}</td><td>{p.wickets}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </section>
    );
}

export default function Teams() {
    const { data, loading } = useLoad(api.standings);
    const [sel, setSel] = useState(null);
    if (loading) return <Loading />;
    if (!data?.length) return <Empty>No teams have registered yet.</Empty>;
    const team = data.find((t) => t.teamId === sel) || data[0];
    return (
        <>
            <h1 className="page-title">Teams</h1>
            <p className="page-sub">Select a team to see its squad.</p>
            <div className="team-grid">
                {data.map((t) => (
                    <button key={t.teamId} className="team-card" aria-pressed={team.teamId === t.teamId} onClick={() => setSel(t.teamId)}>
                        <Badge name={t.teamName} size="lg" />
                        <h3>{t.teamName}</h3>
                        <span className="rec">{t.matchesWon} won, {t.matchesLost} lost, {t.points} pts</span>
                    </button>
                ))}
            </div>
            <Squad team={team} />
        </>
    );
}
