import { useEffect, useState } from 'react';
import { API_BASE_URL, toApiError } from '../api.js';

// Finished reference: one fetch on mount, with loading and error states.
// Read it before TODO-FE-001; then write the Students version yourself instead of copying.
export default function DashboardPage() {
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function loadSummary() {
      try {
        const response = await fetch(`${API_BASE_URL}/api/dashboard`);
        if (!response.ok) {
          throw await toApiError(response);
        }
        setSummary(await response.json());
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }
    loadSummary();
  }, []);

  // TODO-FE-011 (P2): refresh the summary every 30 seconds while this page is open.
  //   Use setInterval inside an effect and return a cleanup function. What happens without cleanup
  //   when you navigate away and back five times?

  if (loading) {
    return <p>Loading dashboard...</p>;
  }
  if (error) {
    return <p className="error">Could not load dashboard: {error}</p>;
  }

  const cards = [
    { label: 'Students', value: summary.students },
    { label: 'Courses', value: summary.courses },
    { label: 'Active enrollments', value: summary.activeEnrollments },
    { label: 'Venues', value: summary.venues },
    { label: 'Bookings', value: summary.bookings },
    { label: 'Announcements', value: summary.announcements },
  ];

  return (
    <section>
      <h2>Dashboard</h2>
      <div className="card-grid">
        {cards.map((card) => (
          <div key={card.label} className="stat-card">
            <div className="stat-value">{card.value}</div>
            <div className="stat-label">{card.label}</div>
          </div>
        ))}
      </div>
    </section>
  );
}
