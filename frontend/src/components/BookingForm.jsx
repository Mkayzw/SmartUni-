// TODO-FE-009: Venue booking form.
//   Props you'll likely need: the list of venues, a list of courses (fetch it here or in the parent: decide),
//   and a callback for when a booking is created.
//   - Fields: venue, course, start, end (<input type="datetime-local">), expected attendance.
//   - datetime-local gives "2030-02-03T09:00". Does Spring's LocalDateTime accept that format? Try it.
//   - Show 409 messages from the backend (overlap, room too small, outside opening hours).
//   - Requires TODO-BE-014/015.
export default function BookingForm() {
  return (
    <form className="form">
      <h3>Book a venue</h3>
      <p className="muted">TODO-FE-009: build this form.</p>
    </form>
  );
}
