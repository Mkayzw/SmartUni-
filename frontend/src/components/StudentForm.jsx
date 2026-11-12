// TODO-FE-002: A controlled form for registering a student.
//   - Fields: firstName, lastName, email. Each <input> gets value={...} and onChange={...}.
//   - On submit: preventDefault, POST JSON to /api/students, and on success call a callback prop
//     (you choose its name) with the created student, then clear the form.
//   - On 400: show the backend's fieldErrors next to the matching input (see toApiError in api.js).
//   - On 409: show the message (duplicate email).
//   - Disable the submit button while the request is in flight.
//
// Requires TODO-BE-001/002/003 on the backend. Until then the POST will return 404/501.
export default function StudentForm() {
  return (
    <form className="form">
      <h3>Register student</h3>
      <p className="muted">TODO-FE-002: build this form.</p>
    </form>
  );
}
