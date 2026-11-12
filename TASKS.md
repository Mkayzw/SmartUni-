# SmartUni practice tasks

You write the code. Every task has an ID that appears in the source as a `TODO-...` comment, and most have tests
tagged with the same ID.

```bash
cd backend
./run-task.sh BE-009      # run only the tests for one task
grep -rn "TODO-BE-009" src  # find every place the task touches
```

Tests for unfinished tasks are tagged `todo` and skipped by a normal `mvn test`, so the baseline build stays green.
A task is done when its tagged tests pass **and** you can explain every annotation you used.

**Entity construction convention:** create new entities with Lombok builders (`Course.builder().code(...).build()`),
as the finished services do. Setters are only for changing an entity you loaded inside a transaction. Every entity has
`@Builder`, a private all-args constructor for the builder, and a protected no-args constructor for JPA. Watch out for
`@Builder.Default` when a field has an initialiser (see `Student.active`).

**Controller convention:** finished controllers return `ResponseEntity` from write endpoints:
`ResponseEntity.created(uri).body(...)` for creates (201 + `Location`), `ResponseEntity.noContent().build()` for deletes.
Follow the same style in the endpoints you add.

**Status codes used across the API:** 400 = malformed or invalid input (bean validation), 404 = referenced record
does not exist, 409 = valid input that breaks a business rule (duplicate, full, overlapping, inactive, in the past).
Using 409 for every business rule is a deliberate simplification. Some teams prefer 422 for rule violations; be ready to discuss it.

---

## Suggested order before Monday

Cover the whole stack early instead of perfecting one layer:

1. **BE-003** Exception handler (fast, unblocks every 409/400 test)
2. **BE-012** `Clock` bean (Spring configuration and DI)
3. **BE-008** Enrollment relationships (JPA)
4. **BE-009** Enrollment business rules (service + repository + transactions)
5. **BE-011** Courses-for-student endpoint (controller + query)
6. **FE-001 / FE-003** Students list with loading and error states (React fetch)
7. **FE-005** Enrollment form consuming BE-009 (React forms + API errors)
8. One live bug and one test-based challenge from `DEBUGGING_CHALLENGES.md`
9. Then the rest of P0, then P1 in any order.

Effort: **S** about 15 min, **M** 30 to 45 min, **L** 60+ min (for someone rusty, not for someone fast).

---

## P0: Interview essentials

### TODO-BE-001: Register a student
- **Requirement:** `POST /api/students` creates an active student. Email must be unique.
- **Files:** `student/StudentService.java`, `student/StudentController.java`
- **Concepts:** `@PostMapping`, `@RequestBody`, `ResponseEntity.created(...)`, `@Transactional`, business exceptions
- **Acceptance:** 201 with a `Location: /api/students/{id}` header and the student JSON; duplicate email returns 409 (needs BE-003).
- **Tests:** `StudentControllerTest` (BE-001)
- **Difficulty:** Easy · **Effort:** M · **Prereqs:** BE-003 for the 409 test · **Monday:** Essential
- **Think about:** check-then-insert is a race. What saves you if two requests with the same email arrive at once?
  What would that failure look like to the client today?

### TODO-BE-002: Validate the student request
- **Requirement:** names must not be blank, email must be present and well-formed.
- **Files:** `student/CreateStudentRequest.java`, `student/StudentController.java`
- **Concepts:** `@NotBlank`, `@NotNull`, `@Email`, `@Valid`, where validation runs (before the method body)
- **Acceptance:** invalid body returns 400; with BE-003 done, `fieldErrors` names each bad field.
- **Tests:** `StudentControllerTest` (BE-002)
- **Difficulty:** Easy · **Effort:** S · **Prereqs:** BE-001 · **Monday:** Essential
- **Think about:** Does `@Email` reject `null`? What is the difference between `@NotNull`, `@NotEmpty` and `@NotBlank`?

### TODO-BE-003: Centralised error responses
- **Requirement:** `BusinessRuleViolationException` becomes 409 and validation failures become 400, both as `ApiError` JSON.
- **Files:** `common/GlobalExceptionHandler.java`
- **Concepts:** `@RestControllerAdvice`, `@ExceptionHandler`, `MethodArgumentNotValidException`, `BindingResult`/`FieldError`
- **Acceptance:** `ApiError.status`, `message`, `path` filled; `fieldErrors` maps field to message.
- **Tests:** `GlobalExceptionHandlerTest` (uses the finished course endpoint, so you can do this first)
- **Difficulty:** Easy/Medium · **Effort:** M · **Prereqs:** none · **Monday:** Essential
- **Think about:** why is this better than try/catch in every controller? What would a catch-all `Exception` handler hide?

### TODO-BE-005: Delete an announcement properly
- **Requirement:** `DELETE /api/announcements/{id}` returns 204, or 404 for an unknown id.
- **Files:** `announcement/AnnouncementService.java`
- **Concepts:** `existsById`/`findById` vs `deleteById`, `ResponseEntity.noContent()` (already in the controller), idempotency of DELETE
- **Acceptance:** tests pass.
- **Tests:** `AnnouncementControllerTest` (BE-005)
- **Difficulty:** Easy · **Effort:** S · **Prereqs:** none · **Monday:** Essential
- **Think about:** DELETE is idempotent. Does returning 404 on the second call break idempotency? (Look up the definition: it's about server state, not the response.)

### TODO-BE-008: Map Enrollment to Student and Course
- **Requirement:** replace `Long studentId` / `Long courseId` with `@ManyToOne` relationships on the same columns.
- **Files:** `enrollment/Enrollment.java` (and anything that stops compiling)
- **Concepts:** `@ManyToOne`, `@JoinColumn`, `FetchType.LAZY`, owning side, foreign keys
- **Acceptance:** metamodel shows two MANY_TO_ONE attributes; seed data still loads (7 enrollments).
- **Tests:** `EnrollmentMappingTest`
- **Difficulty:** Medium · **Effort:** S/M · **Prereqs:** none · **Monday:** Essential
- **Think about:** should `Student` get `@OneToMany(mappedBy = "student") List<Enrollment>`? What does it buy you,
  and what can it cost (accidental loading, `cascade` mistakes, JSON infinite recursion if you ever serialise entities)?
  You do not need it for any test. Decide and justify.

### TODO-BE-009: Enroll a student (business rules)
- **Requirement:** `POST /api/enrollments`: student and course exist (404), student active, no active duplicate, course not full (409).
  Also implement `listEnrollments()`.
- **Files:** `enrollment/EnrollmentService.java`, `enrollment/EnrollmentRepository.java`
- **Concepts:** service-layer rules, derived queries (`existsBy...And...`, `countBy...`), `@Transactional`, DTO mapping
- **Acceptance:** all BE-009 tests pass. `studentName` is "First Last".
- **Tests:** `EnrollmentWorkflowTest` (BE-009)
- **Difficulty:** Medium · **Effort:** L · **Prereqs:** BE-003, BE-008 · **Monday:** Essential
- **Think about:** two students take the last seat at the same moment. Both pass the capacity check. What happens?
  Name two ways to prevent it (pessimistic lock on the course row, optimistic `@Version`, a DB constraint...).
  You don't have to implement it, but you must be able to discuss it.

### TODO-BE-011: Courses for a student
- **Requirement:** `GET /api/students/{studentId}/courses` returns the student's ACTIVE courses; unknown student 404.
- **Files:** `enrollment/EnrollmentController.java`, `enrollment/EnrollmentService.java`, `enrollment/EnrollmentRepository.java`
- **Concepts:** `@GetMapping` with `@PathVariable`, JPQL `select e.course from Enrollment e where ...`, avoiding N+1
- **Acceptance:** student 7 gets CS201 and MATH110, not the cancelled SE350.
- **Tests:** `EnrollmentWorkflowTest` (BE-011)
- **Difficulty:** Medium · **Effort:** M · **Prereqs:** BE-008 · **Monday:** Essential
- **Think about:** why does an empty list for an unknown student mislead clients more than a 404?

### TODO-BE-012: Provide a `Clock` bean
- **Requirement:** a `@Bean` method in `ClockConfig` returning the system clock; the booking service gets "now" from it.
- **Files:** `config/ClockConfig.java`, later `booking/VenueBookingService.java`
- **Concepts:** `@Configuration`, `@Bean`, constructor injection, testability
- **Acceptance:** exactly one `Clock` bean; with BE-014 done, `BookingUsesInjectedClockTest` passes.
- **Tests:** `ClockConfigTest`, `BookingUsesInjectedClockTest`
- **Difficulty:** Easy · **Effort:** S · **Prereqs:** none (second test needs BE-014) · **Monday:** Essential

> **Component scanning vs `@Bean` (know this cold).**
> `@Component` / `@Service` / `@Repository` / `@Controller` go on **your own classes**. At startup Spring scans packages under
> `com.smartuni`, finds those annotations, and calls the constructor itself, resolving constructor parameters from the container.
> `@Bean` goes on a **method** inside a `@Configuration` class. **You** call the constructor or factory and return the object;
> Spring registers whatever you return. Use it when (a) you can't annotate the class, because it's a JDK or library type like
> `java.time.Clock`, `ObjectMapper` or `RestClient`, (b) construction needs logic or configuration values, or (c) you want
> several differently configured instances of one type. Underneath, both end up as bean definitions in the same container.
> `@Configuration` classes are CGLIB-proxied so that calling one `@Bean` method from another returns the singleton instead of a new object.

### TODO-FE-001: Fetch and display students
- **Requirement:** Students page loads `GET /api/students` on mount and renders a table (name links to `/students/:id`).
- **Files:** `frontend/src/pages/StudentsPage.jsx`
- **Concepts:** `useState`, `useEffect` with `[]`, `fetch`, `response.ok`, rendering lists with `key`
- **Acceptance:** 8 seeded students visible, inactive one clearly marked.
- **Difficulty:** Easy · **Effort:** S · **Prereqs:** none · **Monday:** Essential

### TODO-FE-002: Student registration form
- **Requirement:** controlled form posting to `/api/students`; new student appears in the list without a refetch.
- **Files:** `frontend/src/components/StudentForm.jsx`, `StudentsPage.jsx`
- **Concepts:** controlled inputs, `onSubmit` + `preventDefault`, lifting state up, callback props, immutable updates
- **Acceptance:** valid submit adds a row and clears the form; field errors render next to inputs; button disabled while submitting.
- **Difficulty:** Medium · **Effort:** M · **Prereqs:** FE-001, BE-001/002/003 · **Monday:** Essential

### TODO-FE-003: Loading and error states
- **Requirement:** Students page shows loading, then data or a readable error (test by stopping the backend).
- **Files:** `StudentsPage.jsx`
- **Concepts:** conditional rendering, `try/catch/finally`, why `fetch` doesn't reject on 4xx/5xx
- **Difficulty:** Easy · **Effort:** S · **Prereqs:** FE-001 · **Monday:** Essential

### TODO-FE-005: Enrollment form
- **Requirement:** two controlled selects (students, courses) and an Enroll button posting `{studentId, courseId}`; show 409 messages.
- **Files:** `frontend/src/pages/EnrollmentsPage.jsx`
- **Concepts:** parallel fetches (`Promise.all`), select values are strings, error display, updating a list after POST
- **Difficulty:** Medium · **Effort:** M · **Prereqs:** BE-009 · **Monday:** Essential

---

## P1: Intermediate

### TODO-BE-004: Student search
- **Requirement:** `GET /api/students/search?name=ch`: case-insensitive "contains" on first OR last name (not email).
- **Files:** `StudentRepository`, `StudentService`, `StudentController`
- **Concepts:** derived query keywords (`ContainingIgnoreCase`, `Or`), `@RequestParam`, mapping precedence (`/search` vs `/{id}`)
- **Tests:** `StudentControllerTest` (BE-004) · **Difficulty:** Easy/Medium · **Effort:** S · **Monday:** Recommended
- **Think about:** a derived method with `Or` needs the parameter twice. When does a derived name become unreadable enough to switch to `@Query`?

### TODO-BE-006: Real available places
- **Requirement:** `availablePlaces = capacity - active enrollments`.
- **Files:** `course/CourseService.java`, `enrollment/EnrollmentRepository.java`
- **Concepts:** derived `countBy...`, injecting a second repository, cross-feature dependencies
- **Tests:** `CourseControllerTest` (BE-006) · **Difficulty:** Easy · **Effort:** S · **Prereqs:** none (works before or after BE-008) · **Monday:** Recommended
- **Think about:** this runs one count query per course. Fine for 5 courses; what about 500? (See BE-019.)

### TODO-BE-007: Activate / deactivate a student
- **Requirement:** `PATCH /api/students/{id}/status?active=false` returns the updated student.
- **Files:** `StudentService`, `StudentController`
- **Concepts:** `@PatchMapping`, `@PathVariable` + `@RequestParam`, JPA dirty checking inside `@Transactional`
- **Tests:** `StudentControllerTest` (BE-007) · **Difficulty:** Easy · **Effort:** S · **Monday:** Recommended
- **Think about:** after this works, remove `@Transactional` and try again. Explain what you see. (Compare with live bug L02.)

### TODO-BE-010: Cancel an enrollment
- **Requirement:** `PATCH /api/enrollments/{id}/cancel` sets CANCELLED (404 unknown, 409 already cancelled); the student can re-enroll.
- **Files:** `EnrollmentController`, `EnrollmentService`
- **Concepts:** soft delete, state transitions, PATCH vs DELETE
- **Tests:** `EnrollmentWorkflowTest` (BE-010) · **Difficulty:** Easy/Medium · **Effort:** S · **Prereqs:** BE-009 · **Monday:** Recommended

### TODO-BE-013: Typed booking configuration
- **Requirement:** bind `smartuni.booking.*` from `application.yml` to `BookingPolicyProperties` and register it as a bean.
- **Files:** `config/BookingPolicyProperties.java`, a `@Configuration` class or `SmartUniApplication`
- **Concepts:** `@ConfigurationProperties`, `@EnableConfigurationProperties` vs `@ConfigurationPropertiesScan`, relaxed binding (`max-duration-minutes` to `maxDurationMinutes`), constructor binding for records
- **Tests:** `BookingPolicyPropertiesTest` · **Difficulty:** Easy/Medium · **Effort:** S · **Monday:** Recommended
- **Think about:** `@Value("${smartuni.booking.max-duration-minutes}")` also works. Why do teams prefer this?

### TODO-BE-014: Create a venue booking
- **Requirement:** `POST /api/bookings` with all the rules in `VenueBookingService.createBooking` Javadoc (except overlap).
- **Files:** `booking/CreateBookingRequest.java`, `VenueBookingService.java`, `VenueBookingController.java`
- **Concepts:** DTO validation, cross-field rules in the service, `Duration`, `LocalTime` comparisons, using `Clock` and properties
- **Tests:** `VenueBookingRulesTest` (BE-014) · **Difficulty:** Medium · **Effort:** L · **Prereqs:** BE-003, BE-012, BE-013 · **Monday:** Recommended

### TODO-BE-015: Prevent overlapping bookings
- **Requirement:** reject a booking whose half-open range `[start, end)` overlaps an existing booking for the same venue.
- **Files:** `VenueBookingRepository.java`, `VenueBookingService.java`
- **Concepts:** interval overlap condition, JPQL `@Query` with named `@Param`s or a derived `exists` query
- **Tests:** `VenueBookingRulesTest` (BE-015): partial, containing, contained, back-to-back, other venue
- **Difficulty:** Medium · **Effort:** M · **Prereqs:** BE-014 · **Monday:** Recommended
- **Think about:** draw it. Two ranges overlap when `existing.start < new.end AND existing.end > new.start`, but derive it yourself before trusting this line.

### TODO-BE-016: Pin / unpin announcements
- **Requirement:** `PATCH /api/announcements/{id}/pin` with `{"pinned": true}`; list pinned first, then newest.
- **Files:** `AnnouncementController`, `AnnouncementService`, `AnnouncementRepository`
- **Concepts:** small request record, derived ordering `findAllByOrderBy...Desc...Desc`
- **Tests:** `AnnouncementPinTest` · **Difficulty:** Easy · **Effort:** S · **Monday:** Recommended

### TODO-BE-017: Course announcements
- **Requirement:** optional `courseId` on create; response includes `courseId` and `courseCode` (null if campus-wide); unknown course 404.
- **Files:** `Announcement`, `CreateAnnouncementRequest`, `AnnouncementResponse`, `AnnouncementService`
- **Concepts:** optional `@ManyToOne`, nullable foreign keys, LEFT vs INNER joins, lazy loading inside a transaction
- **Tests:** `AnnouncementCourseTest` · **Difficulty:** Medium · **Effort:** M · **Monday:** Optional

### TODO-BE-018: All-or-nothing batch enrollment
- **Requirement:** `POST /api/enrollments/batch` enrolls in several courses; if one fails, none are saved.
- **Files:** `EnrollmentService.enrollInCourses`
- **Concepts:** transaction boundaries, rollback on `RuntimeException`, propagation `REQUIRED`, proxy self-invocation
- **Tests:** `BatchEnrollmentRollbackTest` (deliberately not `@Transactional`; read why)
- **Difficulty:** Medium · **Effort:** M · **Prereqs:** BE-009 · **Monday:** Recommended (classic interview topic)

### TODO-FE-004: CourseCard props
- **Requirement:** render code, title, "X of Y places", or a Full badge when none left.
- **Files:** `frontend/src/components/CourseCard.jsx`
- **Concepts:** props, destructuring, conditional rendering (ternary vs `&&`, and the `0 &&` gotcha)
- **Difficulty:** Easy · **Effort:** S · **Monday:** Recommended

### TODO-FE-006: Enrollment table with cancel
- **Requirement:** list enrollments; Cancel on active rows; replace the row from the PATCH response.
- **Files:** `EnrollmentsPage.jsx`
- **Concepts:** immutable `map` updates, functional `setState`, disabling buttons per row
- **Difficulty:** Medium · **Effort:** M · **Prereqs:** BE-010 · **Monday:** Recommended

### TODO-FE-007: Student detail route
- **Requirement:** `/students/:studentId` shows the student and their courses; Previous/Next links work; unknown id handled.
- **Files:** `StudentDetailPage.jsx`, links from `StudentsPage.jsx`
- **Concepts:** `useParams`, effect dependencies, why the component does **not** remount when only the param changes
- **Difficulty:** Medium · **Effort:** M · **Prereqs:** BE-011 · **Monday:** Recommended

### TODO-FE-008: Announcement pin and delete
- **Requirement:** Delete and Pin/Unpin buttons; keep pinned at the top.
- **Files:** `AnnouncementsPage.jsx`
- **Concepts:** removing/replacing items immutably, sorting a copy (`[...list].sort`), 204 has no body (don't call `.json()`)
- **Difficulty:** Easy/Medium · **Effort:** S · **Prereqs:** BE-005, BE-016 · **Monday:** Optional

### TODO-FE-009: Booking form
- **Requirement:** book a venue with `datetime-local` inputs; show backend rule violations; new booking appears in the table.
- **Files:** `BookingForm.jsx`, `VenuesPage.jsx`
- **Concepts:** component boundaries, passing data and callbacks down, date-time formats across the wire
- **Difficulty:** Medium · **Effort:** M · **Prereqs:** BE-014 · **Monday:** Optional

---

## P2: Advanced (only after P0 feels comfortable)

### TODO-BE-019: Fix the N+1 in booking listing
- **Requirement:** `GET /api/bookings` executes one SQL statement (currently 9).
- **Files:** `VenueBookingRepository`, `VenueBookingService`
- **Concepts:** lazy loading, `join fetch`, `@EntityGraph`, Hibernate statistics, `SQL_LOG_LEVEL=debug`
- **Tests:** `BookingListQueryCountTest` · **Difficulty:** Medium · **Effort:** S/M

### TODO-BE-020: Find available venues
- **Requirement:** `GET /api/venues/available?start&end&minCapacity` returns big-enough venues with no overlapping booking.
- **Files:** `VenueRepository`, `VenueService`
- **Concepts:** JPQL `not exists` subquery, reusing your overlap condition
- **Tests:** `AvailableVenuesTest` · **Difficulty:** Medium/Hard · **Effort:** M

### TODO-BE-021: Profile-specific beans
- **Requirement:** `ConsoleBookingNotifier` only under the `dev` profile, `NoOpBookingNotifier` otherwise; call it after a booking is saved.
- **Files:** `config/BookingNotifier.java` + two new classes, `VenueBookingService`
- **Concepts:** `@Profile("dev")`, `@Profile("!dev")`, programming to an interface, how Spring resolves one bean of a type
- **Tests:** `BookingNotifierProfileTest` · **Difficulty:** Easy/Medium · **Effort:** S

### TODO-BE-022: Write your own repository test
- **Requirement:** a `@DataJpaTest` for your BE-004 search method in `StudentRepositoryTest`, with its own data.
- **Concepts:** test slices, test data ownership, what `@DataJpaTest` does and doesn't load
- **Difficulty:** Easy · **Effort:** S

### TODO-FE-010: Debounced search
- **Requirement:** search box on Students page; request 300 ms after the last keystroke; ignore stale responses.
- **Concepts:** effect cleanup, `setTimeout`/`clearTimeout`, `AbortController`, race conditions
- **Difficulty:** Medium/Hard · **Effort:** M · **Prereqs:** BE-004, FE-001

### TODO-FE-011: Dashboard auto-refresh
- **Requirement:** refresh every 30 s while mounted; no leaked intervals.
- **Concepts:** `setInterval` + cleanup, StrictMode double-invoking effects in development
- **Difficulty:** Easy/Medium · **Effort:** S

### TODO-FE-012: Derived filter
- **Requirement:** "Only courses with places" checkbox; filtered list derived during render, not stored in state.
- **Concepts:** derived state, single source of truth
- **Difficulty:** Easy · **Effort:** S · **Prereqs:** BE-006

---

## Task index

| ID | Priority | Area | Topic |
|---|---|---|---|
| BE-001 | P0 | Spring MVC / service | Register student |
| BE-002 | P0 | Validation | Student DTO constraints |
| BE-003 | P0 | Error handling | `@RestControllerAdvice` |
| BE-005 | P0 | Service / HTTP | Delete with 404 |
| BE-008 | P0 | JPA | `@ManyToOne` mapping |
| BE-009 | P0 | Service / repository | Enrollment rules |
| BE-011 | P0 | Controller / JPQL | Courses for student |
| BE-012 | P0 | Configuration | `Clock` `@Bean` |
| FE-001 | P0 | React | Fetch + list |
| FE-002 | P0 | React | Controlled form |
| FE-003 | P0 | React | Loading / error |
| FE-005 | P0 | React | Enrollment form |
| BE-004 | P1 | Repository | Derived search query |
| BE-006 | P1 | Repository | Count query |
| BE-007 | P1 | JPA | Dirty checking |
| BE-010 | P1 | Service | Cancel enrollment |
| BE-013 | P1 | Configuration | `@ConfigurationProperties` |
| BE-014 | P1 | Service / validation | Booking rules |
| BE-015 | P1 | JPQL | Overlap query |
| BE-016 | P1 | MVC / repository | Pin + ordering |
| BE-017 | P1 | JPA | Optional relationship |
| BE-018 | P1 | Transactions | Rollback |
| FE-004 | P1 | React | Props |
| FE-006 | P1 | React | Immutable updates |
| FE-007 | P1 | React Router | `useParams` + deps |
| FE-008 | P1 | React | Delete/pin |
| FE-009 | P1 | React | Booking form |
| BE-019 | P2 | Performance | N+1 |
| BE-020 | P2 | JPQL | `not exists` |
| BE-021 | P2 | Configuration | `@Profile` |
| BE-022 | P2 | Testing | `@DataJpaTest` |
| FE-010 | P2 | React | Debounce + cleanup |
| FE-011 | P2 | React | Interval cleanup |
| FE-012 | P2 | React | Derived state |
