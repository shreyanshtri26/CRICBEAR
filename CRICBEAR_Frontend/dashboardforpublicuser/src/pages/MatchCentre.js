import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Badge, Empty, Loading, api, oversText, resultLine, short } from '../lib';

const POLL_MS = 3000;

function ballClass(b) {
    if (b.wicket) return 'w';
    if (b.extra) return 'x';
    return b.runsScored === 6 ? 'b6' : b.runsScored === 4 ? 'b4' : '';
}
const ballText = (b) => (b.wicket ? 'W' : b.extra ? (b.extra === 'WIDE' ? 'Wd' : 'Nb') : b.runsScored);

function Overs({ balls }) {
    const byOver = new Map();
    balls.forEach((b) => byOver.set(b.overNumber, [...(byOver.get(b.overNumber) || []), b]));
    let prev = 0;
    const overs = [...byOver.entries()].map(([n, bs]) => {
        const end = bs[bs.length - 1].total;
        const row = { n, bs, runs: end - prev };
        prev = end;
        return row;
    });
    return (
        <div className="overs" role="list" aria-label="Over by over">
            {overs.map(({ n, bs, runs }) => (
                <div className="over" role="listitem" key={n}>
                    <div className="over-h"><span>Over {n + 1}</span><span>{runs} runs</span></div>
                    <div className="balls">{bs.map((b) => <span key={b.bid} className={`ball ${ballClass(b)}`}>{ballText(b)}</span>)}</div>
                </div>
            ))}
        </div>
    );
}

function Scorecard({ batting = [], bowling = [], balls }) {
    const batted = batting.filter((p) => p.ballsFaced > 0 || p.isOut);
    const dnb = batting.filter((p) => !(p.ballsFaced > 0 || p.isOut));
    return (
        <div className="stack">
            <section className="card">
                <div className="table-wrap">
                    <table className="t">
                        <thead><tr><th>Batter</th><th>R</th><th>B</th><th>4s</th><th>6s</th><th>SR</th></tr></thead>
                        <tbody>
                            {batted.map((p) => (
                                <tr key={p.bsid}>
                                    <td><strong>{p.playerName}</strong> <span className="out">{p.isOut ? 'out' : 'not out'}</span></td>
                                    <td className="strong">{p.runsScored}</td><td>{p.ballsFaced}</td><td>{p.fours}</td><td>{p.six}</td>
                                    <td>{p.ballsFaced ? ((p.runsScored / p.ballsFaced) * 100).toFixed(1) : '-'}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
                {dnb.length > 0 && <p className="muted" style={{ padding: '10px 16px', margin: 0 }}>Did not bat: {dnb.map((p) => p.playerName).join(', ')}</p>}
            </section>
            <section className="card">
                <div className="table-wrap">
                    <table className="t">
                        <thead><tr><th>Bowler</th><th>O</th><th>R</th><th>W</th><th>Econ</th></tr></thead>
                        <tbody>
                            {bowling.filter((p) => p.overs > 0).map((p) => (
                                <tr key={p.bsid}>
                                    <td><strong>{p.playerName}</strong></td><td>{p.overs}</td><td>{p.runsGiven}</td>
                                    <td className="strong">{p.wickets}</td><td>{(p.runsGiven / p.overs).toFixed(1)}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </section>
            <section className="card" aria-label="Over by over">
                <div className="card-head"><h2>Over by over</h2></div>
                <Overs balls={balls} />
            </section>
        </div>
    );
}

export default function MatchCentre() {
    const { mid } = useParams();
    const [d, setD] = useState(null);
    const [tab, setTab] = useState(1);

    useEffect(() => {
        let live = true;
        let timer;
        const tick = async () => {
            try {
                const [balls, batting, bowling, result] = await Promise.all([
                    api.balls(mid), api.batting(mid), api.bowling(mid), api.result(mid).catch(() => null),
                ]);
                if (!live) return;
                setD({ balls, batting, bowling, result });
                if (!result) timer = setTimeout(tick, POLL_MS); // still in play: keep refreshing
            } catch (e) {
                if (live) setD({ error: true });
            }
        };
        tick();
        return () => { live = false; clearTimeout(timer); };
    }, [mid]);

    if (!d) return <Loading />;
    if (d.error || !d.balls.Innings1) return <Empty>This match has no scorecard yet. <Link className="link" to="/matches">Back to matches</Link></Empty>;

    const innings = [1, 2].map((n) => d.balls[`Innings${n}`]).filter(Boolean);
    const summary = innings.map((inn) => {
        const last = inn.ballByBall[inn.ballByBall.length - 1] || {};
        const legal = inn.ballByBall.filter((b) => !b.extra).length;
        return { team: inn.battingTeamName, runs: last.total || 0, wkts: last.wicketNumber || 0, overs: oversText(legal) };
    });
    const active = d.balls[`Innings${tab}`] ? tab : 1;
    const t1 = d.balls.Innings1.battingTeamName;
    const t2 = d.balls.Innings1.bowlingTeamName;
    const inLive = !d.result;
    return (
        <>
            <Link className="link" to="/matches">Back to matches</Link>
            <section className="score-hero" style={{ marginTop: 12 }} aria-label="Scoreboard">
                <div className="hero-meta">{inLive ? <span className="pill live">Live</span> : <span className="pill done">Full time</span>}
                    {d.result && <span>Toss: {short(d.result.tossWinnerName)} chose to {d.result.tossDecision.toLowerCase()}</span>}</div>
                <div className="score-line">
                    {[t1, t2].map((team, i) => {
                        const s = summary.find((x) => x.team === team);
                        return (
                            <div key={team} className={`score-side ${i ? 'right' : ''}`}>
                                <Badge name={team} size="lg" />
                                <div>
                                    <div>{team}</div>
                                    {s ? <><div className="score-big">{s.runs}/{s.wkts}</div><div className="score-over">{s.overs} overs</div></>
                                        : <div className="score-over">Yet to bat</div>}
                                </div>
                            </div>
                        );
                    })}
                </div>
                {d.result && <div className="score-result">{resultLine(d.result)}</div>}
            </section>
            <div className="tabs" role="tablist">
                {innings.map((inn, i) => (
                    <button key={inn.battingTeamName} role="tab" aria-selected={active === i + 1} onClick={() => setTab(i + 1)}>
                        {inn.battingTeamName} innings
                    </button>
                ))}
            </div>
            <Scorecard batting={d.batting[`Innings${active}`]} bowling={d.bowling[`Innings${active}`]} balls={d.balls[`Innings${active}`].ballByBall} />
        </>
    );
}
