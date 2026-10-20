import { Link } from 'react-router-dom';
import { Badge, fmtDate, isToday, resultLine, short } from './lib';

export function StandingsTable({ rows, groupOf }) {
    return (
        <div className="table-wrap">
            <table className="t">
                <thead><tr><th>Team</th><th>P</th><th>W</th><th>L</th><th>NRR</th><th>Pts</th></tr></thead>
                <tbody>
                    {rows.map((t, i) => (
                        <tr key={t.teamId} className={groupOf && i < 2 ? 'qual' : ''}>
                            <td><span className="team-cell"><span className="pos">{i + 1}</span><Badge name={t.teamName} size="sm" /><span className="full">{t.teamName}</span><span className="abbr">{short(t.teamName)}</span></span></td>
                            <td>{t.matchesPlayed}</td><td>{t.matchesWon}</td><td>{t.matchesLost}</td>
                            <td>{t.nrr > 0 ? '+' : ''}{t.nrr.toFixed(2)}</td>
                            <td className="strong">{t.points}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

// One fixture. `result` (optional) adds scores and a result line for completed matches.
export function MatchRow({ m, result }) {
    const done = m.status === 'COMPLETED';
    const scoreOf = (team) => {
        if (!result) return null;
        const first = result.battingFirst === team;
        return first ? `${result.runsScoredInFirstinnings}/${result.wicketsInFirstinnings}`
            : `${result.runsScoredInSecondinnings}/${result.wicketsInSecondinnings}`;
    };
    const cls = (team) => (result ? (result.matchWinner === team ? 'won' : 'lost') : '');
    const when = isToday(m.matchDate) ? 'Today' : fmtDate(m.matchDate, { weekday: 'short', day: 'numeric', month: 'short' });
    const Wrap = done || m.status === 'LIVE' ? Link : 'div';
    return (
        <Wrap className="mrow" {...(Wrap === Link ? { to: `/live-match/${m.mid}` } : {})}>
            <div className="mteams">
                {[m.team1, m.team2].map((t) => (
                    <div key={t} className={`mteam ${cls(t)}`}><Badge name={t} size="sm" />{t}<span className="score">{scoreOf(t)}</span></div>
                ))}
            </div>
            <div className="minfo">
                {m.status === 'LIVE' && <span className="pill live">Live</span>}
                {done ? <div className="result-line">{resultLine(result)}</div> : null}
                <div><span className="date-chip">{when}</span> · {m.stadium}</div>
            </div>
        </Wrap>
    );
}
