# SmartUni — Review TODOs (you write the code)

This is a **review of the code currently pushed to `feature/interview-practice-scaffold`** on 2026-11-13, not a replacement for `TASKS.md`. The aim is interview practice: **no prewritten implementations or generated solution patches**.

## How to work

- Implement **one TODO yourself** before asking an assistant for help. First ask for *a hint*, then an explanation of a concept, then a code review; request a full implementation only when deliberately studying a reference.
- For each TODO: **predict -> code -> test -> explain**. Write at least one failure test; make it pass; tell yourself what would go wrong without your change.
- Tests supplied by Claude may not reflect your changed API design. Where a spec and your chosen API differ, decide and document the contract **before** updating a test; never change assertions just to hide a bug.
- GitHub review cannot inspect uncommitted local changes. If you've changed something after pushing, push it before asking for another review.

## Fix the gaps in the currently pushed code

### REVIEW-001 [P0] Make announcement update semantics consistent

**Files:** `announcement/AnnouncementController.java`, `AnnouncementService.java`, `AnnouncementRequest.java`

- [ ] Decide whether `PUT /api/announcements/{id}` means *full replacement* or `PATCH` means *partial update*. The currently pushed controller is **PUT**, not PATCH.
- [ ] For a partial update, changing the title must leave content, pin status, and course unchanged.
- [ ] Test: change only content; verify title and pinned remain as they were.
- [ ] Test: blank supplied title returns 400; an omitted title is allowed for PATCH.
- [ ] Decide how clients can **clear** `courseId` (explicit JSON `null` versus omitted). A plain nullable Java record component cannot distinguish the two. If you need that distinction, design for it deliberately.
- [ ] Explain why calling `saveAnnouncement(announcement, request)` unconditionally is dangerous for PATCH: it always reads title/content, copies the primitive boolean, and can reset the course relation.
- [ ] Keep create-specific validation separate from update-specific validation. Do not remove server-side validation to make the frontend look correct.

**Interview checks:** PUT vs PATCH, idempotency, `@Valid`, `@NotBlank`, `null`, mapping an entity vs constructing one.

### REVIEW-002 [P0] Finish pin/unpin according to the intended API contract

**Files:** `AnnouncementController.java`, `AnnouncementService.java`; existing `AnnouncementPinTest`

- [ ] The existing contract is `PATCH /api/announcements/{id}/pin` with body `{"pinned":true}` or `{"pinned":false}`. Add the missing controller endpoint and request binding.
- [ ] Ensure the service honors the **requested value**, rather than always flipping the previous one. Alternatively, explicitly redesign the endpoint as a toggle and explain why that changes semantics; the provided tests currently check explicit state.
- [ ] Unknown ID -> 404. Pinning twice to `true` -> still pinned.
- [ ] Run `cd backend && ./run-task.sh BE-016`. Investigate failures before altering tests.
- [ ] Explain why setting a state is idempotent but toggling is not.

### REVIEW-003 [P0] Finish announcement ordering and response mapping

**Files:** `AnnouncementRepository.java`, `AnnouncementService.java`, `AnnouncementResponse.java`

- [ ] Pinned first, then newest; choose a deterministic tie-breaker.
- [ ] Test multiple announcements with matching timestamps.
- [ ] Confirm `courseId` and `courseCode` are both null for campus-wide announcements.
- [ ] Explain why mapping a lazy `Course` inside `@Transactional(readOnly = true)` succeeds, and why that does not automatically make it efficient.
- [ ] Compare a `for` loop and `stream().map(...).toList()` for this specific mapper; be able to explain both without memorizing syntax.

### REVIEW-004 [P0] Fix the booking API / React response-shape mismatch

**Files:** `booking/VenueBookingController.java`, `frontend/src/pages/VenuesPage.jsx`

- [ ] The backend now returns `Page<BookingResponse>`; the frontend currently calls `setBookings(await bookingsResponse.json())` and then `bookings.map(...)`.
- [ ] Inspect the real JSON shape in Network or curl; render the records correctly.
- [ ] Show a meaningful empty state and basic next/previous paging.
- [ ] Explain the difference between `List<T>`, `Page<T>`, and `Pageable`.
- [ ] Test first page, last page, and empty page. Consider a stable sort for paging.

### REVIEW-005 [P0] Complete consistent validation-error handling

**Files:** `common/GlobalExceptionHandler.java`, `booking/CreateBookingRequest.java`, controllers

- [ ] Implement the remaining `MethodArgumentNotValidException` handler from BE-003.
- [ ] Ensure invalid booking attendance, missing times, blank titles return useful 400 responses with field errors.
- [ ] Run `./run-task.sh BE-003` and `./run-task.sh BE-014`.
- [ ] Explain why `@NotNull` and `@Min(1)` are both used on attendance; explain when `@Valid` triggers checks.
- [ ] Keep cross-field booking rules (end after start, capacity, overlap) in the service, not single-field annotations.

### REVIEW-006 [P1] Test the completed booking rules at the edges

**Files:** `VenueBookingService.java`, `VenueBookingRepository.java`, new tests you write

- [ ] Run `./run-task.sh BE-014` and `./run-task.sh BE-015` against your branch; do not assume the implementation passed simply because it looks finished.
- [ ] Test a booking exactly at minimum/maximum duration, opening/closing time, and venue capacity.
- [ ] Test back-to-back bookings: the first ends exactly when the second begins.
- [ ] Test two simultaneous booking requests for one venue. Both could pass `existsOverlappingBooking()` before either commits. Describe the race and **one database-backed** way to prevent it.
- [ ] Explain why `Clock` injection helps testing compared with using the system clock everywhere.

### REVIEW-007 [P1] Keep entity construction and modification deliberate

**Files:** `AnnouncementService.java`, `Announcement.java`

- [ ] Compare `new Announcement() + setters` with the repository convention of builders for **new** entities.
- [ ] Decide whether defaulting `pinned` should be a server rule for new announcements instead of trusting the request.
- [ ] For an update, explain JPA dirty checking and why building a new entity with the same ID is not automatically equivalent to modifying the loaded managed entity.
- [ ] Explain `@ManyToOne(fetch = LAZY, optional = true)` and the nullable `course_id` column.

## Add authentication & authorization as **your own** new track

**Scope:** No auth is implemented on the reviewed branch, and `backend/pom.xml` has no Spring Security starter. Implement a **small, testable security slice** rather than building OAuth, a refresh-token service, or a custom identity provider before the interview.

### SEC-001 [P0 / design] Draw the access-control matrix first

- [ ] Define actions for four roles: anonymous visitor, student, lecturer, admin. Decide **which roles may read/create/edit/delete/pin announcements, create bookings, and view enrollments**.
- [ ] Decide if students can see only their own booking or enrollment data.
- [ ] Distinguish **authentication** (who are you?) from **authorization** (may you do this?).
- [ ] State expected HTTP outcomes for anonymous, signed-in-but-forbidden, and permitted requests: 401 / 403 / 2xx.
- [ ] Write examples of both a vertical privilege escalation (student becomes admin) and horizontal access (student sees someone else's record).

### SEC-002 [P1] Learn the Spring Security request pipeline

- [ ] Add the appropriate Spring Security starter.
- [ ] Add a `SecurityFilterChain` configuration that deliberately distinguishes public endpoints from authenticated and privileged endpoints.
- [ ] Use temporary development accounts and a proper `PasswordEncoder`; **do not commit real credentials**.
- [ ] Decide whether the H2 console is allowed only for local development; don't expose it as a general exception.
- [ ] Demonstrate 401, 403, and successful access with curl or MockMvc.
- [ ] Explain the `SecurityContext`, filter chain, principal, and why the backend—not React—enforces access.

**Learning sequence:** Start with Spring Security's built-in mechanisms (such as development-only HTTP Basic) before considering a custom login or token format. Do not mistake Basic authentication for a complete production authentication UX.

### SEC-003 [P1] Enforce roles server-side

- [ ] Protect announcement write endpoints so only permitted roles can call them.
- [ ] Protect booking creation according to your role matrix; keep read visibility intentional.
- [ ] Compare endpoint request-matcher authorization and method security (`@PreAuthorize`); explain when each is useful.
- [ ] A student hiding an admin button must **not** make the underlying API accessible.
- [ ] Check whether existing integration tests need authenticated test users after security is enabled.

### SEC-004 [P1] Write security tests yourself

- [ ] Add `spring-security-test` to the test dependencies if using Spring's test helpers.
- [ ] Write tests for anonymous -> 401, wrong role -> 403, allowed role -> expected success.
- [ ] Write an ownership test: user A cannot access user B's protected record just by changing an ID.
- [ ] Verify a rejected request changes **no database state**.
- [ ] Explain why status assertions alone are not enough for tests about data integrity.

### SEC-005 [P2] Model users without conflating students with credentials

- [ ] Decide whether `Student` is a domain profile while a separate account entity handles login.
- [ ] Add a registration or provisioning workflow with unique identity and hashed password, never plain-text password storage.
- [ ] Keep password hashes out of response DTOs and logging.
- [ ] Consider disabled accounts and role changes. Explain why a `password` field must never appear in a GET response.

### SEC-006 [P2] Add one complete login/logout flow in React

- [ ] Pick **one** supported session or token approach. Don't implement both merely for buzzwords.
- [ ] Add login form, pending/error states, logout, current-user display, and route/UI affordances.
- [ ] Explain cookie/session security, CSRF, CORS, and why hiding a component isn't authorization.
- [ ] Do not store long-lived sensitive tokens in `localStorage` by default or invent custom cryptography.

### SEC-007 [P2] Ownership and trust boundaries

- [ ] Refactor at least one protected action so the server derives the acting user's identity from authenticated context, not a body-supplied user ID.
- [ ] Prove in a test that changing the JSON body cannot impersonate a different student.
- [ ] Document the difference between role checks and ownership checks.

## Additional independent practice (do not ask an assistant to implement these)

### JAVA-REVIEW-001: Optional to exception
- [ ] Write one lookup using `Optional.orElseThrow(...)`. Explain when the lambda executes.

### JAVA-REVIEW-002: Lists and Streams
- [ ] Convert `List<Announcement>` into `List<AnnouncementResponse>` both with a loop and with `stream().map(...).toList()`.
- [ ] Predict every intermediate generic type before running it.

### JAVA-REVIEW-003: Write a MockMvc test
- [ ] Test one successful PATCH and one invalid PATCH using `MockMvc`, without copying the scaffold tests.
- [ ] Assert HTTP status, response JSON, and persisted entity state.

### JAVA-REVIEW-004: REST edge cases
- [ ] Explain 200 vs 201 vs 204; 400 vs 401 vs 403 vs 404 vs 409.
- [ ] Explain why repeated DELETE may return 404 but still satisfy HTTP idempotency.

## Priority order

1. **First:** REVIEW-001, REVIEW-002, REVIEW-004, REVIEW-005 (bugs and API misunderstandings in current work).
2. **Then:** SEC-001 (design only), JAVA-REVIEW-002, JAVA-REVIEW-003 (be able to explain on a whiteboard).
3. **Next:** return to original `TASKS.md` P0 enrollment and React tasks. They are still important interview coverage.
4. **Only if core work is comfortable:** SEC-002 to SEC-004 (small Spring Security slice).
5. **Later:** production-style auth, ownership, token/session choices, concurrency hardening.

## Review notes

Static inspection only: **these changes were not built or executed as part of this review**. Many scaffold tests are tagged `todo` and excluded from normal `mvn test`, so a green default test run does **not** prove your new work is correct. Current tests expect explicit pin state while your service currently toggles, and the default scaffold docs may describe files that have changed on your working branch.

**Rule for future AI assistance:** ask for a review, one hint, or a failing test description; have the assistant stop before providing the full implementation. The goal is that you can write and explain the solution yourself.
