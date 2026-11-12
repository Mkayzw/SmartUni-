import { NavLink, Route, Routes } from 'react-router-dom';
import DashboardPage from './pages/DashboardPage.jsx';
import StudentsPage from './pages/StudentsPage.jsx';
import StudentDetailPage from './pages/StudentDetailPage.jsx';
import CoursesPage from './pages/CoursesPage.jsx';
import EnrollmentsPage from './pages/EnrollmentsPage.jsx';
import VenuesPage from './pages/VenuesPage.jsx';
import AnnouncementsPage from './pages/AnnouncementsPage.jsx';
import ChallengesPage from './challenges/ChallengesPage.jsx';

const navItems = [
  { to: '/', label: 'Dashboard' },
  { to: '/students', label: 'Students' },
  { to: '/courses', label: 'Courses' },
  { to: '/enrollments', label: 'Enrollments' },
  { to: '/venues', label: 'Venues & bookings' },
  { to: '/announcements', label: 'Announcements' },
  { to: '/challenges', label: 'Debugging challenges' },
];

export default function App() {
  return (
    <div className="layout">
      <aside className="sidebar">
        <h1 className="brand">SmartUni</h1>
        <nav>
          {navItems.map((item) => (
            <NavLink key={item.to} to={item.to} end={item.to === '/'} className="nav-link">
              {item.label}
            </NavLink>
          ))}
        </nav>
      </aside>
      <main className="content">
        <Routes>
          <Route path="/" element={<DashboardPage />} />
          <Route path="/students" element={<StudentsPage />} />
          {/* TODO-FE-007: this route exists, the page is a placeholder */}
          <Route path="/students/:studentId" element={<StudentDetailPage />} />
          <Route path="/courses" element={<CoursesPage />} />
          <Route path="/enrollments" element={<EnrollmentsPage />} />
          <Route path="/venues" element={<VenuesPage />} />
          <Route path="/announcements" element={<AnnouncementsPage />} />
          <Route path="/challenges" element={<ChallengesPage />} />
          <Route path="*" element={<p>Page not found.</p>} />
        </Routes>
      </main>
    </div>
  );
}
