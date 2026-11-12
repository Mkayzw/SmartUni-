# SmartUni: interview practice scaffold

A small, realistic campus management app (Spring Boot + React) with deliberate gaps for **you** to implement.
It builds, starts and has a green baseline test suite before you write a line.

- [`TASKS.md`](TASKS.md): 34 exercises (P0/P1/P2) with acceptance criteria and suggested order
- [`DEBUGGING_CHALLENGES.md`](DEBUGGING_CHALLENGES.md): 17 bugs (live, test-based, frontend) with progressive hints
- [`INTERVIEW_THEORY.md`](INTERVIEW_THEORY.md): 97 questions; answers in [`INTERVIEW_THEORY_ANSWERS.md`](INTERVIEW_THEORY_ANSWERS.md)

There are no solution files. If you get stuck, ask for a hint.

## Requirements

Java 21, Maven 3.9+, Node 18+ (tested with Node 22).

## Run it

```bash
# terminal 1: backend on http://localhost:8080 (H2 in-memory, seeded from data.sql)
cd backend
mvn spring-boot:run

# terminal 2: frontend on http://localhost:5173
cd frontend
npm install
npm run dev
```

Useful extras:

```bash
SQL_LOG_LEVEL=debug mvn spring-boot:run                  # print every SQL statement (N+1 hunting)
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run           # dev profile (used by TODO-BE-021)
SPRING_PROFILES_ACTIVE=postgres mvn spring-boot:run      # optional PostgreSQL, see application-postgres.yml
```

H2 console: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:smartuni`, user `sa`, empty password).

## Tests

```bash
cd backend
mvn test                      # baseline only: 15 tests, all green
./run-task.sh BE-009          # tests for one exercise (tagged "todo", skipped by default)
./run-task.sh DBG-B03         # one backend debugging challenge
./run-task.sh todo            # all 58 exercise tests (nearly all fail at the start)
mvn test -Dtest=EnrollmentWorkflowTest -Dgroups=BE-009 -DexcludedGroups=none   # what run-task.sh does
```

Tests are tagged with JUnit 5 `@Tag`. The pom sets `excludedGroups=todo,challenge` so unfinished work doesn't break the build.

## Project structure

```
backend/src/main/java/com/smartuni
├── SmartUniApplication.java
├── common/         ApiError, exceptions, GlobalExceptionHandler (TODO-BE-003)
├── config/         WebConfig (CORS), ClockConfig (BE-012), BookingPolicyProperties (BE-013), BookingNotifier (BE-021)
├── student/        list/get work · register, search, activate are TODO
├── course/         list/get/create work (with a live bug) · availablePlaces is TODO
├── enrollment/     entity without relationships · all service logic is TODO
├── venue/          list/create/update work (with live bugs) · availability search is TODO
├── booking/        finished @ManyToOne example · list works (N+1) · create + overlap are TODO
├── announcement/   finished reference feature · delete, pin, course link are TODO
└── dashboard/      counts (works)
backend/src/test/java/com/smartuni
├── <feature>/      baseline tests + tagged exercise tests
└── challenges/     DBG-B01..B07 (bugs nested inside each test class)

frontend/src
├── App.jsx                 layout + routes
├── api.js                  base URL + toApiError helper
├── pages/                  Dashboard, Students, StudentDetail, Courses, Enrollments, Venues, Announcements
├── components/             StudentForm, CourseCard, BookingForm (TODO skeletons), TodoPanel
└── challenges/             DBG-F01..F05
```

Architecture: feature packages, Controller → Service → Repository, DTO records at the HTTP boundary, constructor injection
via `@RequiredArgsConstructor`, business rules and transactions in services, one `@RestControllerAdvice` for errors.

## What works today

| Area | Works | Your job |
|---|---|---|
| Dashboard | API + screen | FE-011 auto-refresh |
| Announcements | list, create, validation, screen | BE-005 delete, BE-016 pin, BE-017 course link, FE-008 |
| Courses | list, get, create, screen | BE-006 places, FE-004 card, FE-012 filter, live bugs L01 and L04 |
| Venues & bookings | list venues/bookings, create venue, update capacity, screen | BE-014/015 booking rules, BE-019/020, FE-009, live bugs L02, L03, L05 |
| Students | list, get | BE-001/002/004/007, FE-001/002/003/007/010 |
| Enrollments | request validation only | BE-008/009/010/011/018, FE-005/006 |
| Configuration | CORS | BE-012 Clock, BE-013 properties, BE-021 profiles |

Placeholder endpoints throw `UnsupportedOperationException("TODO-...")`, which the handler maps to **501 Not Implemented**
so the UI shows a clear message. Placeholder screens show a yellow TODO panel.

## Where to start

Do **TODO-BE-003** first: open `backend/src/main/java/com/smartuni/common/GlobalExceptionHandler.java`, read the two TODOs,
and make `./run-task.sh BE-003` pass. It's quick and makes every later 400/409 test meaningful. Then follow
"Suggested order before Monday" in `TASKS.md`.

## Seed data you'll lean on

| | |
|---|---|
| CS101 (id 1) | capacity 3, **full** (students 1, 2, 3) |
| CS201 (id 2) | students 1 and 7 |
| SE350 (id 5) | student 7 **cancelled** |
| Student 8 | **inactive** |
| Venue 1, 2030-02-03 08:00–10:00 | existing booking (overlap tests) |

## Verified when this scaffold was created

- `mvn test`: 15 baseline tests pass.
- `./run-task.sh todo` plus challenges: 69 tests compile and run; 63 fail or error as intended. The 6 that pass are
  correct-behaviour checks whose bug is in a sibling test (e.g. DBG-B06 "accepts a valid event") or 404s that already
  happen because the endpoint isn't mapped yet.
- Packaged jar starts; `GET /api/dashboard`, `/api/courses`, `/api/venues`, `/api/bookings` respond; CORS preflight from `localhost:5173` allowed.
- `npm run build` succeeds; every screen renders against the live backend with no console errors (checked in headless Chromium).
- Live bugs L01, L02 and L03 reproduced with curl.
- Not verified: the PostgreSQL profile (no Postgres in the build environment).
