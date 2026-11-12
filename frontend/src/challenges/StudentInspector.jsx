import { useEffect, useState } from 'react';
import { API_BASE_URL } from '../api.js';

// Expected: choosing a different student id loads that student's details.
export default function StudentInspector() {
  const [studentId, setStudentId] = useState(1);
  const [student, setStudent] = useState(null);

  useEffect(() => {
    fetch(`${API_BASE_URL}/api/students/${studentId}`)
      .then((response) => response.json())
      .then((data) => setStudent(data));
  }, []);

  return (
    <div>
      <label>
        Student id{' '}
        <select value={studentId} onChange={(event) => setStudentId(Number(event.target.value))}>
          {[1, 2, 3, 4, 5, 6, 7, 8].map((id) => (
            <option key={id} value={id}>{id}</option>
          ))}
        </select>
      </label>
      {student && (
        <p>
          #{student.id} {student.firstName} {student.lastName} ({student.email})
        </p>
      )}
    </div>
  );
}
