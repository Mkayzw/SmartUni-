import { useEffect, useState } from 'react';
import { API_BASE_URL } from '../api.js';

const SAFETY_LIMIT = 25;

// Expected: one request when you press Start (and again only when "active only" changes).
// Watch the request counter and the browser's Network tab. The safety limit stops it hammering the backend.
export default function FilteredStudents() {
  const [started, setStarted] = useState(false);
  const [activeOnly, setActiveOnly] = useState(true);
  const [students, setStudents] = useState([]);
  const [requestCount, setRequestCount] = useState(0);

  const filters = { activeOnly };

  useEffect(() => {
    if (!started || requestCount >= SAFETY_LIMIT) {
      return;
    }
    fetch(`${API_BASE_URL}/api/students`)
      .then((response) => response.json())
      .then((data) => {
        setRequestCount((current) => current + 1);
        setStudents(filters.activeOnly ? data.filter((student) => student.active) : data);
      });
  }, [started, filters]);

  return (
    <div>
      <button type="button" onClick={() => setStarted(true)}>Start</button>{' '}
      <label>
        <input type="checkbox" checked={activeOnly} onChange={(event) => setActiveOnly(event.target.checked)} /> Active only
      </label>
      <p>Requests sent: {requestCount}{requestCount >= SAFETY_LIMIT && ' (safety limit hit)'}</p>
      <p>{students.length} students</p>
    </div>
  );
}
