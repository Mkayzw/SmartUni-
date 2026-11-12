# Interview theory: model answers

These are short model answers, not scripts. Say them in your own words and attach a SmartUni example.

## 1. Java core and OOP
1. **Encapsulation:** private fields behind methods (`Student`). **Abstraction:** exposing what, hiding how (`JpaRepository` hides SQL). **Inheritance:** `BusinessRuleViolationException extends RuntimeException`. **Polymorphism:** `BookingNotifier` calls go to whichever implementation is injected.
2. Interface = a contract with no state; a class can implement many. Abstract class = shared state and partial implementation; single inheritance. Since Java 8, interfaces can have `default` and `static` methods, so prefer interfaces unless you need shared fields or constructors.
3. The booking service depends on the abstraction, so swapping console/no-op/email implementations (or a mock in tests) needs no change to the service. That's the Dependency Inversion Principle.
4. Invariants. Callers can't put an object into an invalid state or depend on its internals. Entities need a no-arg constructor and accessible properties for JPA, so getters/setters are the pragmatic compromise.
5. Inheritance couples every subclass to the base class's implementation; generic base services leak abstractions and grow conditionals. Composition (inject what you need) keeps each class explicit and independently testable.
6. Equal objects must have equal hash codes. For entities, the id is null before persist and set after, so an id-based hash changes while the object sits in a `HashSet`. Common fixes: business key, or equals on id with a constant `hashCode()`.
7. An immutable data carrier with generated constructor, accessors, `equals`/`hashCode`/`toString`. Perfect for request/response DTOs. JPA needs a no-arg constructor, mutable state and often proxies (subclassing), and records are final and immutable.
8. `final`: can't reassign/override/extend. `finally`: block that always runs after try/catch. `finalize`: deprecated GC hook; don't use it.
9. Static method/field belongs to the class, not instances. A static nested class doesn't hold a reference to an outer instance, so it can be instantiated alone (Spring and MockMvc can create it).

## 2. Collections and generics
10. Students in a course: `List` (or `Set` if you need uniqueness without order). Unique codes: `Set`. Field to error: `Map<String, String>`.
11. `ArrayList` by default: contiguous memory, O(1) random access, cache-friendly. `LinkedList` rarely wins in practice.
12. Array of buckets indexed by `hash(key)`. Collisions chain in a list, converted to a balanced tree when a bucket gets large (Java 8+). Lookup uses `hashCode` to find the bucket, `equals` to find the entry.
13. An unmodifiable list: `add` throws `UnsupportedOperationException`. `stream().toList()` also returns an unmodifiable list.
14. Compile-time type safety without casts. Erasure: generic types are removed at runtime, so you can't do `new T()` or `instanceof List<String>`.
15. A repository whose entity type is `Student` and whose id type is `Long`, so `findById(Long)` returns `Optional<Student>`.

## 3. Exceptions
16. Checked exceptions must be declared or caught (recoverable conditions, e.g. `IOException`). Unchecked (`RuntimeException`) needn't be (programming errors or conditions handled centrally).
17. It travels up through service and controller to the `@RestControllerAdvice` without polluting every signature, and Spring's `@Transactional` rolls back on unchecked exceptions by default.
18. Closes resources automatically, in reverse order, even on exceptions. The resource implements `AutoCloseable`.
19. It swallows failures silently, so data corruption or broken features go unnoticed. Catch the specific exception you can handle, or log and rethrow, or let it propagate to a central handler.

## 4. Optional and streams
20. It makes "might be absent" explicit in the return type so callers must handle it. `orElseThrow` converts absence into a meaningful exception that becomes a 404.
21. Don't use it for fields (not serialisable, adds overhead), method parameters (callers pass null anyway), or collections (return an empty collection instead).
22. `map` transforms each element one-to-one; `flatMap` maps each element to a stream and flattens the results.
23. Intermediate ops (`map`, `filter`) build a pipeline and run nothing until a terminal op (`toList`, `count`, `forEach`) pulls elements through.
24. When you need early exit with complex conditions, checked exceptions, multiple accumulators, or index-aware logic. The enrollment rules are clearer as `if` statements than as a stream.

## 5. Spring and dependency injection
25. IoC: the framework, not your code, controls object creation and wiring. DI is the specific technique: dependencies are passed in (usually via constructor) rather than created inside. DI is one way to achieve IoC.
26. Constructor injection makes dependencies explicit and `final`, guarantees a fully initialised object, works in plain unit tests (`new Service(mockRepo)`), and exposes too many dependencies as a code smell. Field injection hides dependencies and needs reflection to test.
27. A constructor taking every `final` (and `@NonNull`) field. Since Spring 4.3, a class with a single constructor has it used for autowiring automatically.
28. Functionally all are `@Component` for scanning. `@Repository` additionally enables persistence exception translation. `@Controller` is detected by MVC as a handler. `@Service` is semantic only.
29. `@Component` goes on your class and Spring constructs it. `@Bean` is a factory method where you construct it. You must use `@Bean` for classes you can't annotate (`Clock`, library types) or when construction needs logic or multiple configured instances.
30. A class declaring `@Bean` methods. It's subclassed with CGLIB so that calling `clock()` from another `@Bean` method returns the container's singleton instead of creating a second instance.
31. Singleton (one per container). Prototype (new instance per injection). Web scopes: request, session. Prototype for stateful, short-lived helpers; request scope for per-request context.
32. Instantiate, inject dependencies, Aware callbacks, `BeanPostProcessor` before-init, `@PostConstruct`/`afterPropertiesSet`, post-processor after-init (where AOP proxies are created), in use, `@PreDestroy` on shutdown.
33. By type. If several match: a `@Qualifier` on the injection point, a `@Primary` bean, or a parameter name matching the bean name (needs `-parameters`, which Boot enables). Otherwise `NoUniqueBeanDefinitionException`.
34. `@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`. Scanning starts at the annotated class's package and includes sub-packages.
35. Conditional configuration classes shipped with Boot (`@ConditionalOnClass`, `@ConditionalOnMissingBean`...). H2 on the classpath plus no user-defined DataSource triggers an embedded DataSource. Your own beans back the defaults off.
36. `@ConfigurationProperties`: typed, grouped, validatable (`@Validated`), relaxed binding, IDE metadata, one place to see all settings. `@Value`: fine for one-off values; stringly-typed, scattered, no grouping.
37. A named set of beans/config activated per environment, via `spring.profiles.active`, `SPRING_PROFILES_ACTIVE`, or `@ActiveProfiles` in tests. `@Profile("!dev")` registers the bean when `dev` is not active.
38. A needs B and B needs A. With constructor injection neither can be constructed first, so startup fails immediately (Boot 2.6+ forbids cycles by default). That's good: cycles signal a design problem.

## 6. Spring MVC and REST
39. Tomcat hands the request to `DispatcherServlet`, which asks `HandlerMapping`s for a match (path + method), and gets a `HandlerAdapter`. Argument resolvers bind `@PathVariable`/`@RequestParam`/`@RequestBody` (HttpMessageConverter/Jackson) and run validation. The method runs; the return value is converted to JSON. Exceptions go to `HandlerExceptionResolver`s including `@ControllerAdvice`.
40. Path variable: identifies a resource (`/students/7`). Request param: filters or options (`?name=ch`). Request body: the resource representation for create/update (JSON).
41. `@RestController` = `@Controller` + `@ResponseBody`: return values are written to the response body rather than resolved as view names.
42. `@ResponseStatus` sets a fixed status. `ResponseEntity` is needed when status or headers vary at runtime, e.g. `201` with a `Location` header.
43. `ExceptionHandlerExceptionResolver` finds the most specific `@ExceptionHandler` across advice beans and uses its return value as the response. Unhandled exceptions reach Boot's `/error` handling, and the client sees a 500 (or the status from `@ResponseStatus` on the exception).
44. 201, 204, 400, 404, 409, 409 (or 422), 500, 501.
45. POST creates (not idempotent). PUT replaces the full resource (idempotent). PATCH partially updates (not necessarily idempotent). DELETE is idempotent: idempotency is about server state after repeated calls, not identical responses, so a 404 on the second call is fine.
46. A browser security mechanism: scripts from origin A can't read responses from origin B unless B sends `Access-Control-Allow-Origin`. The browser enforces it; curl isn't a browser, so it doesn't.
47. Decouple API contract from schema; avoid leaking fields (passwords, internals); avoid lazy-loading and infinite recursion during serialisation; shape responses per use case (`studentName`); separate validation rules per operation.

## 7. Validation
48. During argument resolution, before your method body. Failure throws `MethodArgumentNotValidException`, by default a 400.
49. `@NotNull`: not null ("" allowed). `@NotEmpty`: not null and length > 0. `@NotBlank`: not null and contains a non-whitespace character (strings only).
50. To avoid unexpected deep object traversal and cycles. Mark the field (or record component) with `@Valid` to cascade.
51. In the service, or with a custom class-level constraint. A single-field annotation only sees one value. The service also needs other rules like overlap and capacity, which need the database.

## 8. JPA and Hibernate
52. JPA: the specification (annotations, `EntityManager`). Hibernate: the most common implementation. Spring Data JPA: repositories on top of JPA that generate implementations from interfaces.
53. The first-level cache of managed entities for a unit of work (the `EntityManager`). Transient: new, unknown to JPA. Managed: tracked, changes flushed. Detached: was managed, context closed, no longer tracked.
54. On flush, Hibernate compares managed entities with their snapshots and issues UPDATEs automatically. In L02 the entity is loaded in the repository's own short transaction and modified after it closed (detached), so nothing flushes.
55. The side with the foreign key owns it: the `@ManyToOne` side (`enrollments.student_id`). `mappedBy` on the `@OneToMany` says "the other side's field owns this; I'm just a view."
56. `@ManyToOne`/`@OneToOne` default EAGER; `@OneToMany`/`@ManyToMany` default LAZY. Prefer LAZY everywhere and fetch what each use case needs; EAGER loads data you didn't ask for and can't be undone per query.
57. One query for the list, then one more per row to load a lazy association. Detect with SQL logging or Hibernate statistics. Fix with `join fetch`, `@EntityGraph`, batch fetching, or a DTO projection query.
58. Accessing a lazy proxy after its persistence context closed. OSIV keeps the context open for the whole web request so views can lazy-load. It hides N+1s and holds DB connections during rendering, so we turn it off and load what we need inside the service transaction.
59. Spring parses the method name into a query (`findBy`, properties, keywords like `ContainingIgnoreCase`, `OrderBy`) when the repository is created. An unknown property fails startup with `PropertyReferenceException`.
60. When the method name gets unreadable, or you need joins, subqueries, projections, `not exists`. JPQL is entity- and field-based and portable; native SQL uses table/column names and DB-specific features.
61. Propagates persist/merge/remove to the related entity. On `@ManyToOne`, removing an enrollment would delete the student. Use cascade from parent to owned children only.
62. IDENTITY: the DB generates the id on insert, so Hibernate must insert immediately (no JDBC batching). SEQUENCE: fetches ids ahead from a sequence, allowing batching. Preferred on Postgres.

## 9. Transactions
63. Spring wraps the bean in a proxy. The `TransactionInterceptor` begins a transaction (via `PlatformTransactionManager`) before the method, binds it to the thread, and commits or rolls back after.
64. `RuntimeException` and `Error` roll back; checked exceptions commit. Change with `rollbackFor = Exception.class` or `noRollbackFor`.
65. Self-invocation calls `this.method()`, bypassing the proxy. Private methods can't be overridden or intercepted by proxies.
66. Hints to the JPA provider and driver: Hibernate skips dirty checking and flushes; some DBs optimise. It doesn't by itself prevent writes in every setup.
67. How a transactional method behaves when called within an existing transaction. `REQUIRED` joins it (default). `REQUIRES_NEW` suspends it and starts an independent one, e.g. writing an audit log that must persist even if the outer transaction rolls back.
68. Atomicity (all or nothing), Consistency (constraints hold), Isolation (concurrent transactions don't see each other's partial work), Durability (committed data survives crashes). Dirty read: reading uncommitted data. Non-repeatable read: same row, different value. Phantom: same query, different rows.
69. Both read count = capacity - 1 and both insert: over-capacity. Prevent with a pessimistic lock on the course row (`@Lock(PESSIMISTIC_WRITE)` select), an optimistic `@Version` counter on the course, or a DB-level constraint/trigger. Plus a unique constraint for duplicates.
70. Service. It's where a use case starts and ends; controllers shouldn't know about persistence, and repository-level transactions are too fine-grained to make multi-step operations atomic.

## 10. SQL and databases
71. INNER returns only rows matching in both tables; LEFT returns all left rows with nulls when no match. Campus-wide announcements have no course, so listing needs a LEFT JOIN.
72. A data structure (usually a B-tree) for fast lookups by column. It costs write speed and storage. Candidates: `students.email` (unique), `enrollments(course_id, status)`, `enrollments(student_id, course_id)`, `venue_bookings(venue_id, start_time)`.
73. Correctness under concurrency. The check-then-insert race can't slip through a constraint enforced by the database.
74. `SELECT c.code, COUNT(e.id) FROM courses c LEFT JOIN enrollments e ON e.course_id = c.id AND e.status = 'ACTIVE' GROUP BY c.id, c.code;`. Note the status filter goes in the ON clause, not WHERE, or zero-enrollment courses disappear.
75. Organising data to remove redundancy and update anomalies. A stored counter can drift out of sync with enrollments; computing it keeps one source of truth (you'd denormalise only with a measured performance need).

## 11. Testing
76. Unit: plain JUnit + Mockito, no Spring (`CourseServiceTest`). Slice: `@WebMvcTest` (MVC only), `@DataJpaTest` (JPA + embedded DB). Integration: `@SpringBootTest` loads the full context (`EnrollmentWorkflowTest`).
77. `@Mock` creates a Mockito mock in a plain unit test. `@MockitoBean` replaces a bean inside a Spring test context (e.g. the service behind a `@WebMvcTest` controller).
78. A test-level transaction wraps the request and is always rolled back, so you can't observe whether your own transaction boundary rolled back.
79. Dependence on time, ordering, shared state, network, randomness. Injecting `Clock` lets tests freeze "now".

## 12. React
80. A function that returns UI for its inputs. Props are the inputs passed by the parent; read-only so data flows one way and the parent stays the source of truth.
81. Data a component remembers between renders. Mutating it in place keeps the same reference, so React thinks nothing changed and may skip re-rendering; it also breaks predictable updates.
82. A state setter called with a new value (by `Object.is`), a parent re-rendering, or context changes.
83. Runs after render to sync with external systems (fetch, timers, subscriptions). Deps: re-run when any listed value changes; `[]` = once after mount. Cleanup runs before the next effect run and on unmount.
84. A function that captured variables from the render it was created in and keeps seeing those old values.
85. The first uses the value from this render (three calls = +1). The updater form receives the latest pending state, so three calls = +3.
86. Controlled: React state is the source of truth (`value` + `onChange`). Uncontrolled: the DOM holds the value (read via ref). Controlled makes validation and reset easy.
87. Keys let React match items between renders. Index keys tie DOM/state to positions, so after reordering, state (like uncontrolled input values) stays at the old position and attaches to a different item.
88. It resolves normally with `response.ok === false`. It only rejects on network failure. Check `response.ok`, parse the error body, and show it.
89. Moving state to the closest common parent so siblings share it; children receive values and callbacks via props. Own state when only that component needs it.
90. A value computable from existing props/state (a filtered list). Storing it creates two sources of truth that can drift; compute it during render (use `useMemo` only if expensive).
91. To surface effects that lack proper cleanup or aren't idempotent. It mounts, unmounts and remounts once in development only.
92. It reads params from the current route match. Navigating from `/students/1` to `/students/2` reuses the same component instance, so an effect with `[]` won't re-run. Depend on the param.

## 13. Debugging technique
93. Reproduce reliably; read the server log's root cause ("Caused by"); check the request in the Network tab (method, URL, body); find the failing line; form one hypothesis and test it with the smallest change or a breakpoint.
94. Rendering code: wrong field name, stale state, missing key, effect dependencies, mutated state. Use React DevTools to inspect props/state.
95. Skip framework frames, go to the bottom "Caused by", find the first frame in `com.smartuni`, read the exception message literally.
96. Inspect the actual HTTP exchange. If the response is wrong, it's the backend (confirm with curl). If the response is right but the UI is wrong, it's the frontend.
97. Compare environments: config/profiles, data, versions, env vars, time zones, caches. Make the difference explicit, then eliminate variables one at a time.

## 14. Lombok
98. `@Builder` generates a builder that calls an all-args constructor, so one must exist; making it private forces callers through the builder. Declaring any constructor removes Java's implicit no-args one, but JPA needs a no-args constructor to instantiate entities and create lazy proxies, so it's added back as protected (the minimum JPA allows). Remove the all-args constructor and `@Builder` won't compile next to `@NoArgsConstructor`; remove the no-args one and Hibernate fails at runtime.
99. `@Builder` ignores field initialisers, so `active` would default to `false`, the Java default for `boolean`. `@Builder.Default` makes the builder use the initialiser.
100. It generates `equals`/`hashCode` over all fields (mutable, includes the generated id and lazy associations) and `toString` over associations, which can trigger lazy loading or infinite recursion. Prefer explicit `@Getter`/`@Setter`.
