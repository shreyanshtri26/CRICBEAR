import { StandingsTable, MatchRow } from '../components';
import { Empty, Loading, api, fmtDate, loadSeason, useLoad } from '../lib';

async function load() {
    const [{ t, matches }, standings, groups] = await Promise.all([loadSeason(), api.standings(), api.groups()]);
    return { t, matches, standings, groups };
}

const STATUS = { LIVE: 'Live', COMPLETED: 'Completed', UPCOMING: 'Upcoming' };

export default function Tournament() {
    const { data, loading } = useLoad(load);
    if (loading) return <Loading />;
    if (!data?.t) return <Empty>No tournament yet.</Empty>;
    const { t, matches, standings, groups } = data;
    const groupOf = Object.fromEntries(groups.filter((g) => g.tid === t.tid).map((g) => [g.teamid, g.groupNumber]));
    const numbers = [...new Set(Object.values(groupOf))].sort();
    const knockouts = matches.filter((m) => m.type !== 'NORMAL');
    return (
        <>
            <h1 className="page-title">{t.tournamentName}</h1>
            <p className="page-sub">
                <span className={`pill ${t.status === 'LIVE' ? 'live' : 'done'}`}>{STATUS[t.status]}</span>{' '}
                {fmtDate(t.startDate, { day: 'numeric', month: 'long' })} to {fmtDate(t.endDate, { day: 'numeric', month: 'long', year: 'numeric' })}.
                The top two teams in each group reach the semi-finals.
            </p>
            <div className="grid-2" style={{ gridTemplateColumns: 'repeat(auto-fit, minmax(min(100%, 340px), 1fr))' }}>
                {(numbers.length ? numbers : [0]).map((n) => (
                    <section className="card" key={n} aria-label={n ? `Group ${String.fromCharCode(64 + n)}` : 'Standings'}>
                        <div className="card-head"><h2>{n ? `Group ${String.fromCharCode(64 + n)}` : 'Standings'}</h2></div>
                        <StandingsTable groupOf rows={standings.filter((s) => !n || groupOf[s.teamId] === n)} />
                    </section>
                ))}
            </div>
            {knockouts.length > 0 && (
                <section className="card" style={{ marginTop: 20 }}>
                    <div className="card-head"><h2>Knockouts</h2></div>
                    <div className="rows">{knockouts.map((m) => <MatchRow key={m.mid} m={m} />)}</div>
                </section>
            )}
        </>
    );
}
