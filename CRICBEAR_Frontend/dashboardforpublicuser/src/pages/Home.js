import { Link } from 'react-router-dom';
import { StandingsTable, MatchRow } from '../components';
import { Badge, Empty, Loading, api, fmtDate, isToday, loadResults, loadSeason, useLoad } from '../lib';

async function load() {
    const { t, matches } = await loadSeason();
    const [standings, results] = await Promise.all([api.standings(), loadResults(matches)]);
    return { t, matches, standings, results };
}

export default function Home() {
    const { data, loading, error } = useLoad(load);
    if (loading) return <Loading />;
    if (error || !data.t) return <Empty>No tournament is running yet. Check back soon.</Empty>;
    const { t, matches, standings, results } = data;
    const live = matches.find((m) => m.status === 'LIVE');
    const upcoming = matches.filter((m) => m.status === 'UPCOMING');
    const featured = live || upcoming[0];
    const recent = matches.filter((m) => m.status === 'COMPLETED').slice(-5).reverse();
    return (
        <>
            {featured && (
                <section className="hero" aria-label={live ? 'Live match' : 'Next match'}>
                    <div className="hero-meta">
                        {live ? <span className="pill live">Live now</span> : <span className="pill">{isToday(featured.matchDate) ? 'Today' : 'Next match'}</span>}
                        <span>{t.tournamentName}</span>
                        <span>{fmtDate(featured.matchDate, { weekday: 'long', day: 'numeric', month: 'long' })}</span>
                        <span>{featured.stadium}</span>
                    </div>
                    <div className="hero-teams">
                        <div className="hero-team"><Badge name={featured.team1} size="lg" /><h2>{featured.team1}</h2></div>
                        <span className="hero-vs">VS</span>
                        <div className="hero-team right"><Badge name={featured.team2} size="lg" /><h2>{featured.team2}</h2></div>
                    </div>
                    {live
                        ? <Link className="btn" to={`/live-match/${featured.mid}`}>Follow the match</Link>
                        : <Link className="btn ghost" to="/matches">See all fixtures</Link>}
                </section>
            )}
            <div className="grid-2">
                <section className="card" aria-labelledby="st">
                    <div className="card-head"><h2 id="st">Points table</h2><Link to="/tournaments">Full standings</Link></div>
                    <StandingsTable rows={standings} />
                </section>
                <div className="stack">
                    <section className="card" aria-labelledby="rr">
                        <div className="card-head"><h2 id="rr">Latest results</h2><Link to="/matches">All matches</Link></div>
                        <div className="rows">
                            {recent.length ? recent.map((m) => <MatchRow key={m.mid} m={m} result={results[m.mid]} />) : <Empty>No results yet.</Empty>}
                        </div>
                    </section>
                    <section className="card" aria-labelledby="uf">
                        <div className="card-head"><h2 id="uf">Coming up</h2></div>
                        <div className="rows">
                            {upcoming.length ? upcoming.slice(0, 4).map((m) => <MatchRow key={m.mid} m={m} />) : <Empty>No fixtures scheduled.</Empty>}
                        </div>
                    </section>
                </div>
            </div>
        </>
    );
}
