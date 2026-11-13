import { useState } from 'react';
import { API_BASE_URL, toApiError } from '../api.js';

// Expected: enrolls student 5 in course 3 (works once TODO-BE-009 is done). Right now even a valid request
// is rejected, and the problem is NOT in the backend's business logic.
export default function QuickEnroll() {
  const [result, setResult] = useState('');

  async function enroll() {
    setResult('Sending...');
    const response = await fetch(`${API_BASE_URL}/api/enrollments`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ student_id: 5, course_id: 3 }),
    });
    if (!response.ok) {
      const error = await toApiError(response);
      setResult(`Failed (${error.status}): ${error.message}`);
      return;
    }
    setResult('Enrolled!');
  }

  return (
    <div>
      <button type="button" onClick={enroll}>Enroll student 5 in CS301</button>
      <p>{result}</p>
    </div>
  );
}
