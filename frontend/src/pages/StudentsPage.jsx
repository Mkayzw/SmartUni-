import TodoPanel from '../components/TodoPanel.jsx';
import StudentForm from '../components/StudentForm.jsx';

export default function StudentsPage() {
  // TODO-FE-001: Fetch GET /api/students when the page mounts and render a table:
  //   name (as a <Link> to /students/:id for TODO-FE-007), email, and an Active/Inactive badge.
  //   Decide what state you need. Ask yourself: what should the dependency array be, and why?
  //
  // TODO-FE-003: Show a loading message while fetching and an error message if the request fails
  //   (stop the backend to test it). Don't leave the user staring at an empty table.
  //
  // TODO-FE-002: Render <StudentForm /> and, when it reports a newly created student, add that student to the
  //   list WITHOUT refetching. Hint: the form shouldn't own the list. Who should, and how does the form tell them?
  //
  // TODO-FE-010 (P2): Add a search box that calls GET /api/students/search?name=... as the user types,
  //   waiting 300ms after the last keystroke. Clean up the timer in the effect's cleanup function.

  return (
    <section>
      <h2>Students</h2>
      <TodoPanel taskId="TODO-FE-001 / FE-002 / FE-003">
        Fetch and display students, add the registration form, handle loading and errors.
      </TodoPanel>
      <StudentForm />
    </section>
  );
}
