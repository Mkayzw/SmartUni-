import { Link } from 'react-router-dom';
import TodoPanel from '../components/TodoPanel.jsx';

// TODO-FE-007: Student detail page at /students/:studentId
//   - Read studentId with useParams().
//   - Fetch GET /api/students/{id} and GET /api/students/{id}/courses (TODO-BE-011).
//   - Show the student's details and their active courses.
//   - Add "Previous" / "Next" links to /students/{id-1} and /students/{id+1}.
//     Click them: does the data update? If not, look at your effect's dependency array.
//   - Unknown id: show "Student not found" (the backend returns 404).
export default function StudentDetailPage() {
  return (
    <section>
      <Link to="/students">Back to students</Link>
      <h2>Student</h2>
      <TodoPanel taskId="TODO-FE-007">Build the student detail page.</TodoPanel>
    </section>
  );
}
