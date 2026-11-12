import { useEffect, useState } from 'react';
import { API_BASE_URL, toApiError } from '../api.js';
import CourseCard from '../components/CourseCard.jsx';

const emptyForm = { code: '', title: '', capacity: 30 };

export default function CoursesPage() {
  const [courses, setCourses] = useState([]);
  const [error, setError] = useState(null);
  const [form, setForm] = useState(emptyForm);
  const [formError, setFormError] = useState(null);
  const [selectedCourse, setSelectedCourse] = useState(null);

  useEffect(() => {
    fetch(`${API_BASE_URL}/api/courses`)
      .then((response) => (response.ok ? response.json() : Promise.reject(new Error('Failed to load courses'))))
      .then(setCourses)
      .catch((err) => setError(err.message));
  }, []);

  // TODO-FE-012 (P2): add an "Only show courses with places" checkbox.
  //   Do NOT store the filtered list in state. Derive it during render. Why is a second useState a bug magnet?

  function handleChange(event) {
    const { name, value } = event.target;
    setForm({ ...form, [name]: value });
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setFormError(null);
    try {
      const response = await fetch(`${API_BASE_URL}/api/courses`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ courseCode: form.code, title: form.title, capacity: Number(form.capacity) }),
      });
      if (!response.ok) {
        throw await toApiError(response);
      }
      const created = await response.json();
      setCourses([...courses, created]);
      setForm(emptyForm);
    } catch (err) {
      setFormError(err.message);
    }
  }

  async function handleViewDetails(courseId) {
    setSelectedCourse(null);
    const response = await fetch(`${API_BASE_URL}/api/courses/${courseId}`);
    if (!response.ok) {
      const apiError = await toApiError(response);
      setSelectedCourse({ error: apiError.message });
      return;
    }
    setSelectedCourse(await response.json());
  }

  return (
    <section>
      <h2>Courses</h2>
      {error && <p className="error">{error}</p>}

      <div className="course-list">
        {courses.map((course) => (
          <CourseCard key={course.id} course={course} onViewDetails={handleViewDetails} />
        ))}
      </div>

      {selectedCourse && (
        <div className="details">
          {selectedCourse.error ? (
            <p className="error">{selectedCourse.error}</p>
          ) : (
            <p>
              {selectedCourse.code}: {selectedCourse.title} (capacity {selectedCourse.capacity})
            </p>
          )}
        </div>
      )}

      <form className="form" onSubmit={handleSubmit}>
        <h3>New course</h3>
        <label>
          Code
          <input name="code" value={form.code} onChange={handleChange} placeholder="CS450" />
        </label>
        <label>
          Title
          <input name="title" value={form.title} onChange={handleChange} />
        </label>
        <label>
          Capacity
          <input name="capacity" type="number" min="1" value={form.capacity} onChange={handleChange} />
        </label>
        {formError && <p className="error">{formError}</p>}
        <button type="submit">Create course</button>
      </form>
    </section>
  );
}
