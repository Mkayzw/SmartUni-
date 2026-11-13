# Debugging challenges

Treat each one like the practical part of the interview: **reproduce, observe, hypothesise, verify, fix, explain.**
Write your hypothesis down before opening a hint. Hints get more specific as you go; none of them give the fix.

There are three kinds:

- **Live bugs (L):** in the running application. Nothing in the code is marked. Start from the symptom.
- **Backend challenges (B):** JUnit tests in `backend/src/test/java/com/smartuni/challenges/`. Run with `./run-task.sh DBG-B01`.
  The buggy code is nested inside each test class. The test itself is correct; don't change the assertions.
- **Frontend challenges (F):** the "Debugging challenges" screen in the app, code in `frontend/src/challenges/`.

None of these stop the application from starting.

A general toolkit worth practising out loud: read the **whole** stack trace from the bottom "Caused by"; check the HTTP status and
response body in the browser Network tab before reading code; turn on SQL logging (`SQL_LOG_LEVEL=debug`); put a breakpoint
on the first line you're sure runs; make the smallest change that tests a hypothesis.

---

## Live bugs

### L01: Course details always fail
On the Courses screen, click **Details** on any course. You get an error instead of the course. `curl -i localhost:8080/api/courses/1` too.

<details><summary>Hint 1</summary>Read the status code and the error body carefully. Is the server complaining about the course, or about the request?</details>
<details><summary>Hint 2</summary>The message talks about a missing parameter. Where in the URL is the id, and where is Spring looking for it?</details>
<details><summary>Hint 3</summary>Compare the controller method's parameter annotation with the one used on <code>StudentController.getStudent</code>.</details>

### L02: Venue capacity "saves" but doesn't stick
On Venues, change Seminar Room's capacity to 25 and click Save. The table shows 25. Refresh the page: it's back to 20.

<details><summary>Hint 1</summary>The response contained 25, so the Java object was changed. Was the database? Run with SQL logging on and look for an UPDATE.</details>
<details><summary>Hint 2</summary>Compare <code>VenueService.updateCapacity</code> with <code>createVenue</code>. What does one have that the other doesn't?</details>
<details><summary>Hint 3</summary>Dirty checking only happens while the entity is managed by an open persistence context, and changes are flushed at commit. When does that persistence context close in this method? (Remember <code>open-in-view</code> is false.) There are two valid fixes; know both and pick one.</details>

### L03: Nonsense venues are accepted
`POST /api/venues` with `"capacity": -5` and a blank name returns 201. `CreateVenueRequest` clearly has `@Min(1)` and `@NotBlank`.

<details><summary>Hint 1</summary>Annotations on a DTO are just metadata. Something has to ask a validator to check them.</details>
<details><summary>Hint 2</summary>Compare the <code>createVenue</code> controller method signature with <code>CourseController.createCourse</code>.</details>

### L04: Creating a course from the UI always fails
Fill in the New course form with valid values. It fails with a 400. The same values work with curl:
`curl -X POST localhost:8080/api/courses -H 'Content-Type: application/json' -d '{"code":"CS450","title":"AI","capacity":30}'`

<details><summary>Hint 1</summary>If curl works and the browser doesn't, compare exactly what each sends. Open the Network tab, find the POST, and look at the request payload.</details>
<details><summary>Hint 2</summary>Jackson silently ignores JSON properties it doesn't recognise (Spring Boot's default). So a misspelled property doesn't fail; it just leaves the Java field null.</details>

### L05: New venue doesn't appear until refresh
Register a new venue. No error, the backend returns 201, but the table doesn't change. After a page refresh the venue is there.

<details><summary>Hint 1</summary>The server did its job. This is a rendering problem. When does React re-render after a state setter is called?</details>
<details><summary>Hint 2</summary>React compares the new state with the old one using <code>Object.is</code>. Look at what is passed to <code>setVenues</code> in <code>handleCreateVenue</code>. Is it a new array?</details>

---

## Backend challenges (tests)

### DBG-B01: Timetable endpoints (Spring MVC mappings) · `./run-task.sh DBG-B01`
`GET /challenge/timetable/CS101` gives 400 and `POST /challenge/timetable/entries` gives 405.

<details><summary>Hint 1</summary>Two different bugs. 400 means a mapping matched but binding failed. 405 means the path matched but the HTTP method didn't.</details>
<details><summary>Hint 2</summary>For the GET: where does the course code live in the URL, path or query string? For the POST: read the <code>@RequestMapping</code> attributes.</details>

### DBG-B02: Two email senders (bean injection) · `./run-task.sh DBG-B02`
The context fails to start. Requirement: keep both senders; the registration mailer must use the sandbox one.

<details><summary>Hint 1</summary>Read the exception name and message at the bottom of the trace. How many candidates did Spring find for the constructor parameter?</details>
<details><summary>Hint 2</summary>Spring resolves by type first. When there are several, it needs a tie-breaker. There are at least three ways to give it one: one on the parameter, one on a bean definition, and one that relies on names.</details>
<details><summary>Hint 3</summary>Which tie-breaker is most explicit for this requirement ("this mailer uses sandbox"), and which one changes the default for <em>every</em> injection point? Interviewers like hearing that trade-off.</details>

### DBG-B03: Onboarding doesn't roll back (`@Transactional`) · `./run-task.sh DBG-B03`
The method is `@Transactional` and throws, yet the student stays in the database.

<details><summary>Hint 1</summary>Look at the type hierarchy of <code>WelcomeEmailException</code>.</details>
<details><summary>Hint 2</summary>By default, Spring's transaction interceptor rolls back on some exception types and commits on others. Which?</details>
<details><summary>Hint 3</summary>Read the attributes of <code>@Transactional</code>. Also be ready to explain two other classic reasons a <code>@Transactional</code> annotation silently does nothing: calling the method from inside the same class, and putting it on a private method. Why do both fail? (Think proxies.)</details>

### DBG-B04: Booking report explodes (lazy loading) · `./run-task.sh DBG-B04`
`LazyInitializationException: could not initialize proxy ... no Session`.

<details><summary>Hint 1</summary>Which line touches a lazy association? Is a transaction or persistence context open at that moment?</details>
<details><summary>Hint 2</summary><code>findAll()</code> runs in its own short transaction inside the repository. After it returns, the entities are detached.</details>
<details><summary>Hint 3</summary>There are two good fixes (keep the context open for the whole method, or load the association eagerly in the query) and one bad one (turning <code>open-in-view</code> on or switching the mapping to EAGER). Explain why the bad ones are bad.</details>

### DBG-B05: Room finder misses the right room (repository query) · `./run-task.sh DBG-B05`
For 35 attendees, Computer Lab 1 (35 seats) is missing, and the biggest venue comes first.

<details><summary>Hint 1</summary>Read the JPQL in <code>ChallengeVenueRepository</code> next to its Javadoc. Two separate things disagree with the requirement.</details>
<details><summary>Hint 2</summary>Boundary values: can a 35-seat room hold 35 people? And which direction is "smallest first"?</details>

### DBG-B06: Nested validation (bean validation) · `./run-task.sh DBG-B06`
`@Valid` is on the controller parameter, `@Email` is on the organiser's email, and an invalid email is still accepted.

<details><summary>Hint 1</summary>Validation found and checked <code>title</code> and <code>organiser</code> (try sending a null organiser). So it runs, but it stops somewhere.</details>
<details><summary>Hint 2</summary>Bean Validation does not walk into nested objects unless told to. How do you tell it, and where does that marker go?</details>

### DBG-B07: Wrong status codes (HTTP semantics) · `./run-task.sh DBG-B07`
Unknown rooms return 200 with an empty body; deleting returns 200 "Deleted" even for rooms that never existed.

<details><summary>Hint 1</summary><code>Map.get</code> and <code>Map.remove</code> both tell you whether the key was there. Is the controller using that information?</details>
<details><summary>Hint 2</summary>Which status means "succeeded, nothing to return"? Which <code>ResponseEntity</code> builder methods produce 404 and 204?</details>

---

## Frontend challenges (Debugging challenges screen)

### DBG-F01: Counter that cannot count (`StaleCounter.jsx`)
"+3" adds 1. "Start auto" goes to 1 and stays there.

<details><summary>Hint 1</summary>Inside one event handler, what is the value of <code>count</code> on each of the three lines?</details>
<details><summary>Hint 2</summary>A state setter accepts either a value or a function. What does the function form receive?</details>
<details><summary>Hint 3</summary>For the interval: the callback was created during one particular render. Which <code>count</code> did it capture, and does the effect ever re-run to capture a newer one? Fix it without adding <code>count</code> to the dependency array, then explain why adding it would also work but is worse.</details>

### DBG-F02: Inspector stuck on one student (`StudentInspector.jsx`)
Changing the student id in the dropdown doesn't change the details shown.

<details><summary>Hint 1</summary>The select updates state, so the component re-renders. Does the effect run again?</details>
<details><summary>Hint 2</summary>Read the dependency array as a sentence: "re-run this effect when ... changes." Is that sentence true to what the effect uses? Bonus: what happens if you switch ids quickly and an old response arrives last?</details>

### DBG-F03: Roster shows blanks and swaps notes (`StudentRoster.jsx`)
Names are blank. Type a note next to the first student, click Reverse order: the note stays in the first row, now next to a different student.

<details><summary>Hint 1</summary>Two bugs. For the blanks: compare the field the JSX reads with the JSON from <code>GET /api/students</code>.</details>
<details><summary>Hint 2</summary>For the notes: the inputs are uncontrolled, so the DOM holds their values. React decides which DOM node belongs to which item by <code>key</code>. What is the key, and does it follow the student when the order changes?</details>

### DBG-F04: Quick enroll always fails (`QuickEnroll.jsx`)
Returns 400 even though student 5 and course 3 are perfectly valid. (Do this after BE-009 and BE-003 for the clearest message.)

<details><summary>Hint 1</summary>Read the response body's message and fieldErrors. Which fields does the backend think are missing?</details>
<details><summary>Hint 2</summary>Compare the JSON property names sent with the record components of <code>CreateEnrollmentRequest</code>. Then decide which side should change, and why.</details>

### DBG-F05: Filter that never stops fetching (`FilteredStudents.jsx`)
Press Start. The request counter climbs to the safety limit.

<details><summary>Hint 1</summary>Something in the dependency array changes on every render. Which one, and why is it "different" each time even though its contents are the same?</details>
<details><summary>Hint 2</summary>Each fetch sets state, which re-renders, which creates a new object, which re-runs the effect... What should the effect really depend on?</details>
