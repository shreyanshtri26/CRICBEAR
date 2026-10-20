import { BrowserRouter, NavLink, Route, Routes } from 'react-router-dom';
import Home from './pages/Home';
import Matches from './pages/Matches';
import MatchCentre from './pages/MatchCentre';
import Teams from './pages/Teams';
import Players from './pages/Players';
import Tournament from './pages/Tournament';

const ADMIN_URL = process.env.REACT_APP_ADMIN_URL || '/admin';

const icon = (d) => <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">{d}</svg>;
const LINKS = [
    ['/', 'Home', icon(<path d="M3 11l9-8 9 8v10a1 1 0 0 1-1 1h-5v-7H9v7H4a1 1 0 0 1-1-1z" />), true],
    ['/matches', 'Matches', icon(<><rect x="3" y="4" width="18" height="17" rx="2" /><path d="M3 10h18M8 2v4M16 2v4" /></>)],
    ['/tournaments', 'Table', icon(<path d="M4 20V10M10 20V4M16 20v-8M22 20H2" />)],
    ['/teams', 'Teams', icon(<><circle cx="9" cy="8" r="3.5" /><path d="M2 21c0-4 3-6 7-6s7 2 7 6M17 4.5a3.5 3.5 0 0 1 0 7M22 21c0-3-1.5-5-4-5.7" /></>)],
    ['/playerprofile', 'Players', icon(<><circle cx="12" cy="7" r="4" /><path d="M4 21c0-4.5 3.5-7 8-7s8 2.5 8 7" /></>)],
];

function Shell({ children }) {
    return (
        <>
            <header className="topbar">
                <div className="topbar-inner">
                    <NavLink to="/" className="brand" aria-label="CRICBEAR home">
                        <img className="brand-mark" src={`${process.env.PUBLIC_URL}/mark.png`} alt="" />
                        CRICBEAR
                    </NavLink>
                    <nav className="nav" aria-label="Primary">
                        {LINKS.map(([to, label, , end]) => <NavLink key={to} to={to} end={end}>{label === 'Table' ? 'Standings' : label}</NavLink>)}
                    </nav>
                    <a className="admin-link" href={ADMIN_URL}>Admin login</a>
                </div>
            </header>
            <main className="page">{children}</main>
            <nav className="tabbar" aria-label="Primary">
                {LINKS.map(([to, label, svg, end]) => <NavLink key={to} to={to} end={end}>{svg}{label}</NavLink>)}
            </nav>
        </>
    );
}

export default function App() {
    return (
        <BrowserRouter>
            <Shell>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/matches" element={<Matches />} />
                    <Route path="/tournaments" element={<Tournament />} />
                    <Route path="/teams" element={<Teams />} />
                    <Route path="/playerprofile" element={<Players />} />
                    <Route path="/live-match/:mid" element={<MatchCentre />} />
                    <Route path="*" element={<p className="empty">That page doesn't exist. <a className="link" href="/">Go to the home page</a></p>} />
                </Routes>
            </Shell>
        </BrowserRouter>
    );
}
