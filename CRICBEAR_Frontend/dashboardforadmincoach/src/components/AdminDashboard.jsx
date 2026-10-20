import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles.css';

function AdminDashboard() {
    const navigate = useNavigate();

    // Handle user logout
    const handleLogout = () => {
        localStorage.removeItem('user');
        navigate('/login');
    };

    return (
        <div className="dashboard-container">
            {/* Sidebar navigation for admin functions */}
            <nav className="sidebar">
                <ul>
                    <li><Link to="./TournamentManagement">Tournament Management</Link></li>
                    <li><Link to="./ManageUser">Manage User</Link></li>
                    <li><Link to="./ManageMatch">Manage Match</Link></li>
                </ul>
            </nav>
            <div className="dashboard-content">
                {/* Logout button and welcome message */}
                <button onClick={handleLogout} className="logout-button">Logout</button>
                <h2>Welcome, Admin</h2>
                <p className="lead">Choose what you want to manage.</p>
                <div className="cards">
                    <Link className="tile" to="./TournamentManagement"><h3>Tournaments</h3><p>Create tournaments, start them and generate the schedule.</p></Link>
                    <Link className="tile" to="./ManageMatch"><h3>Matches</h3><p>Start today's matches and run the semi-finals and final.</p></Link>
                    <Link className="tile" to="./ManageUser"><h3>Users</h3><p>Review accounts and change coach or admin roles.</p></Link>
                </div>

            </div>
        </div>
    );
}

export default AdminDashboard;
