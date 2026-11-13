import TodoPanel from '../components/TodoPanel.jsx';

// TODO-FE-005: Enrollment form
//   - Load students AND courses when the page mounts (two requests; can they run in parallel?).
//   - Two <select> elements (controlled) plus an "Enroll" button.
//   - POST /api/enrollments with { studentId, courseId } as NUMBERS. What type does <select>.value give you?
//   - On 409 show the backend's message ("course is full", "already enrolled"...). On 201 add the row to the table.
//
// TODO-FE-006: Enrollment table
//   - GET /api/enrollments. Columns: student, course, status, enrolled at.
//   - A "Cancel" button on ACTIVE rows calling PATCH /api/enrollments/{id}/cancel.
//   - After cancelling, replace that one row in state with the response (immutably: map, don't mutate).
//   - Bonus: why might setEnrollments(enrollments.map(...)) behave differently from
//     setEnrollments(current => current.map(...)) if two cancels finish close together?
//
// Backend prerequisites: TODO-BE-008, BE-009, BE-010.
export default function EnrollmentsPage() {
  return (
    <section>
      <h2>Enrollments</h2>
      <TodoPanel taskId="TODO-FE-005 / FE-006">Build the enrollment form and the enrollment table with cancel.</TodoPanel>
    </section>
  );
}
