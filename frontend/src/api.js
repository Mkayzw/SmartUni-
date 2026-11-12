// The backend runs on port 8080; Vite on 5173. The backend's WebConfig allows this origin (CORS).
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

// Turns a non-2xx fetch Response into an Error carrying the backend's ApiError fields.
// fetch() only rejects on NETWORK failure. A 404 or 409 still "succeeds", so you must check response.ok yourself.
export async function toApiError(response) {
  let body = null;
  try {
    body = await response.json();
  } catch {
    // Body was empty or not JSON (e.g. Spring's default error page). Fall through.
  }
  const error = new Error(body?.message ?? `Request failed with status ${response.status}`);
  error.status = response.status;
  error.fieldErrors = body?.fieldErrors ?? {};
  return error;
}
