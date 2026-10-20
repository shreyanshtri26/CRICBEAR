import { useState } from 'react';
import { MatchRow } from '../components';
import { Empty, Loading, loadResults, loadSeason, useLoad } from '../lib';

const TABS = [['LIVE', 'Live'], ['UPCOMING', 'Upcoming'], ['COMPLETED', 'Results']];

async function load() {
    const s = await loadSeason();
    return { ...s, results: await loadResults(s.matches) };
}

export default function Matches() {
    const { data, loading } = useLoad(load);
    const [tab, setTab] = useState('UPCOMING');
    const [q, setQ] = useState('');
    if (loading) return <Loading />;
    const { matches = [], results = {} } = data || {};
    const list = matches.filter((m) => m.status === tab && `${m.team1} ${m.team2} ${m.stadium}`.toLowerCase().includes(q.toLowerCase()));
    if (tab === 'COMPLETED') list.reverse();
    return (
        <>
            <h1 className="page-title">Matches</h1>
            <p className="page-sub">{data?.t?.tournamentName}</p>
            <div className="toolbar">
                <div className="seg" role="group" aria-label="Match status">
                    {TABS.map(([k, label]) => <button key={k} aria-pressed={tab === k} onClick={() => setTab(k)}>{label}</button>)}
                </div>
                <input className="search" type="search" placeholder="Search team or stadium" aria-label="Search matches" value={q} onChange={(e) => setQ(e.target.value)} />
            </div>
            <div className="card rows wide">
                {list.length
                    ? list.map((m) => <MatchRow key={m.mid} m={m} result={results[m.mid]} />)
                    : <Empty>{tab === 'LIVE' ? 'No match is live right now.' : 'No matches found.'}</Empty>}
            </div>
        </>
    );
}
