import { useEffect, useState } from 'react';
import { API_BASE_URL } from '../api.js';

// Expected: every row shows the student's full name. Notes typed next to a student stay with that student
// after clicking "Reverse order".
export default function StudentRoster() {
  const [students, setStudents] = useState([]);

  useEffect(() => {
    fetch(`${API_BASE_URL}/api/students`)
      .then((response) => response.json())
      .then(setStudents);
  }, []);

  return (
    <div>
      <button type="button" onClick={() => setStudents([...students].reverse())}>Reverse order</button>
      <ul className="roster">
        {students.map((student, index) => (
          <li key={index}>
            <span className="roster-name">{student.name}</span>
            <input placeholder="Notes" />
          </li>
        ))}
      </ul>
    </div>
  );
}
