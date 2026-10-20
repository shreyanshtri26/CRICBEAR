import axios from 'axios';

// Rewrites the dev default URLs to the deployed backends (set at build time).
const { REACT_APP_AUTH_URL: auth, REACT_APP_TOURNAMENT_URL: tournament } = process.env;
axios.interceptors.request.use((c) => {
    if (auth) c.url = c.url.replace('http://localhost:8083', auth);
    if (tournament) c.url = c.url.replace('http://localhost:8084', tournament);
    return c;
});
