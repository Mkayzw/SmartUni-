import { useEffect, useState } from 'react';
import { API_BASE_URL, toApiError } from '../api.js';

// Finished reference for list + create. Pin/unpin and delete are TODO-FE-008.
export default function AnnouncementsPage() {
  const [announcements, setAnnouncements] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    async function loadAnnouncements() {
      try {
        const response = await fetch(`${API_BASE_URL}/api/announcements`);
        if (!response.ok) throw await toApiError(response);
        setAnnouncements(await response.json());
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }
    loadAnnouncements();
  }, []);

  async function handleSubmit(event) {
    event.preventDefault();
    setSubmitting(true);
    setError(null);
    try {
      const response = await fetch(`${API_BASE_URL}/api/announcements`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title, content }),
      });
      if (!response.ok) throw await toApiError(response);
      const created = await response.json();
      setAnnouncements((current) => [created, ...current]);
      setTitle('');
      setContent('');
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  }

  // TODO-FE-008:
  //   1. A "Delete" button per announcement: DELETE /api/announcements/{id} (204), then remove it from state.
  //   2. A "Pin"/"Unpin" toggle: PATCH /api/announcements/{id}/pin with {"pinned": true|false}.
  //      Replace the updated item in state, and keep pinned items at the top (sort a COPY, never the state array).
  //   3. Show a pin marker on pinned announcements.
  //   Backend prerequisites: TODO-BE-005, TODO-BE-016.

  if (loading) {
    return <p>Loading announcements...</p>;
  }

  return (
    <section>
      <h2>Announcements</h2>
      {error && <p className="error">{error}</p>}

      <form className="form" onSubmit={handleSubmit}>
        <h3>New announcement</h3>
        <label>
          Title
          <input value={title} onChange={(event) => setTitle(event.target.value)} />
        </label>
        <label>
          Content
          <textarea value={content} onChange={(event) => setContent(event.target.value)} rows={3} />
        </label>
        <button type="submit" disabled={submitting}>
          {submitting ? 'Posting...' : 'Post announcement'}
        </button>
      </form>

      {announcements.length === 0 ? (
        <p className="muted">No announcements yet.</p>
      ) : (
        <ul className="announcement-list">
          {announcements.map((announcement) => (
            <li key={announcement.id} className={announcement.pinned ? 'announcement pinned' : 'announcement'}>
              <h4>{announcement.title}</h4>
              <p>{announcement.content}</p>
              <small className="muted">{announcement.createdAt.replace('T', ' ').slice(0, 16)}</small>
            </li>
          ))}
        </ul>
      )}
    </section>
  );
}
