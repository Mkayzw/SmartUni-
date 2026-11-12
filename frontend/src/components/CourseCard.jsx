// TODO-FE-004: Render a single course from props.
//   Show: code, title, "X of Y places available".
//   When availablePlaces is 0, show a "Full" badge (className "badge badge-danger") instead of the count.
//   The course object comes from the parent. This component must not fetch anything.
//   (availablePlaces is only correct after TODO-BE-006.)
export default function CourseCard({ course, onViewDetails }) {
  return (
    <div className="course-card">
      <strong>{course.code}</strong>
      <button type="button" className="link-button" onClick={() => onViewDetails(course.id)}>
        Details
      </button>
    </div>
  );
}
