import { useEffect, useState } from 'react';
import { API_BASE_URL, toApiError } from '../api.js';
import BookingForm from '../components/BookingForm.jsx';

const emptyVenue = { name: '', building: '', roomNumber: '', capacity: 30 };

export default function VenuesPage() {
  const [venues, setVenues] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [error, setError] = useState(null);
  const [newVenue, setNewVenue] = useState(emptyVenue);
  const [capacityEdits, setCapacityEdits] = useState({});

  useEffect(() => {
    async function load() {
      try {
        const [venuesResponse, bookingsResponse] = await Promise.all([
          fetch(`${API_BASE_URL}/api/venues`),
          fetch(`${API_BASE_URL}/api/bookings`),
        ]);
        if (!venuesResponse.ok) throw await toApiError(venuesResponse);
        if (!bookingsResponse.ok) throw await toApiError(bookingsResponse);
        setVenues(await venuesResponse.json());
        setBookings(await bookingsResponse.json());
      } catch (err) {
        setError(err.message);
      }
    }
    load();
  }, []);

  function handleVenueChange(event) {
    const { name, value } = event.target;
    setNewVenue({ ...newVenue, [name]: value });
  }

  async function handleCreateVenue(event) {
    event.preventDefault();
    setError(null);
    try {
      const response = await fetch(`${API_BASE_URL}/api/venues`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ...newVenue, capacity: Number(newVenue.capacity) }),
      });
      if (!response.ok) throw await toApiError(response);
      const created = await response.json();
      venues.push(created);
      setVenues(venues);
      setNewVenue(emptyVenue);
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleSaveCapacity(venueId) {
    const capacity = Number(capacityEdits[venueId]);
    const response = await fetch(`${API_BASE_URL}/api/venues/${venueId}/capacity`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ capacity }),
    });
    if (!response.ok) {
      setError((await toApiError(response)).message);
      return;
    }
    const updated = await response.json();
    setVenues((current) => current.map((venue) => (venue.id === updated.id ? updated : venue)));
  }

  return (
    <section>
      <h2>Venues</h2>
      {error && <p className="error">{error}</p>}
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Building</th>
            <th>Room</th>
            <th>Capacity</th>
            <th>Change capacity</th>
          </tr>
        </thead>
        <tbody>
          {venues.map((venue) => (
            <tr key={venue.id}>
              <td>{venue.name}</td>
              <td>{venue.building}</td>
              <td>{venue.roomNumber}</td>
              <td>{venue.capacity}</td>
              <td>
                <input
                  type="number"
                  className="small-input"
                  value={capacityEdits[venue.id] ?? ''}
                  onChange={(event) => setCapacityEdits({ ...capacityEdits, [venue.id]: event.target.value })}
                />
                <button type="button" onClick={() => handleSaveCapacity(venue.id)}>
                  Save
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <form className="form" onSubmit={handleCreateVenue}>
        <h3>Register venue</h3>
        <label>
          Name
          <input name="name" value={newVenue.name} onChange={handleVenueChange} />
        </label>
        <label>
          Building
          <input name="building" value={newVenue.building} onChange={handleVenueChange} />
        </label>
        <label>
          Room number
          <input name="roomNumber" value={newVenue.roomNumber} onChange={handleVenueChange} />
        </label>
        <label>
          Capacity
          <input name="capacity" type="number" value={newVenue.capacity} onChange={handleVenueChange} />
        </label>
        <button type="submit">Register venue</button>
      </form>

      <h2>Bookings</h2>
      <table>
        <thead>
          <tr>
            <th>Venue</th>
            <th>Course</th>
            <th>Start</th>
            <th>End</th>
            <th>Attendance</th>
          </tr>
        </thead>
        <tbody>
          {bookings.map((booking) => (
            <tr key={booking.id}>
              <td>{booking.venueName}</td>
              <td>{booking.courseCode}</td>
              <td>{booking.startTime.replace('T', ' ')}</td>
              <td>{booking.endTime.replace('T', ' ')}</td>
              <td>{booking.expectedAttendance}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <BookingForm />
    </section>
  );
}
