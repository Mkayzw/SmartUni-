# Interview theory: questions

Answer out loud, in two or three sentences, then give a concrete example **from this project**. Model answers live in
[`INTERVIEW_THEORY_ANSWERS.md`](INTERVIEW_THEORY_ANSWERS.md). Don't open it until you've answered a whole section.

Questions marked ★ are tied directly to code you'll write. Prioritise those.

## 1. Java core and OOP
1. What are the four pillars of OOP? Give one example of each from this codebase or from Java's standard library.
2. Interface vs abstract class: when would you choose each? What changed with default methods in Java 8?
3. ★ `BookingNotifier` is an interface with two implementations (BE-021). What does programming to an interface buy you here?
4. What is encapsulation actually protecting? Why do the entities use private fields with getters and setters instead of public fields?
5. Composition vs inheritance: why does the project avoid a `BaseService` or `BaseController`?
6. What is the contract between `equals()` and `hashCode()`? Why is it tricky for JPA entities with generated ids?
7. ★ What is a Java `record`? Why are records a good fit for DTOs but a poor fit for JPA entities?
8. `final`, `finally`, `finalize`: what does each mean?
9. What does `static` mean on a method, a field, and a nested class? (The challenge tests use static nested classes. Why?)

## 2. Collections and generics
10. `List` vs `Set` vs `Map`: pick the right one for "students in a course", "unique course codes", and "field name to error message".
11. `ArrayList` vs `LinkedList`: which do you use by default and why?
12. How does `HashMap` work internally? What happens on a collision?
13. What does `List.of(...)` return, and what happens if you call `add` on it? Where does `stream().toList()` fit?
14. What are generics for? What is type erasure and one practical consequence?
15. What does `JpaRepository<Student, Long>` mean in terms of generics?

## 3. Exceptions
16. Checked vs unchecked exceptions: what's the difference and when would you use each?
17. ★ Why is `BusinessRuleViolationException` a `RuntimeException`? (Two reasons: API ergonomics and transactions.)
18. What does `try-with-resources` do? What interface must the resource implement?
19. Why is `catch (Exception e) { }` dangerous? What should you do instead?

## 4. Optional and streams
20. ★ What problem does `Optional` solve? Explain `findById(id).orElseThrow(() -> new ResourceNotFoundException(...))`.
21. When should you **not** use `Optional` (fields, parameters, collections)?
22. `map` vs `flatMap` on a stream.
23. Intermediate vs terminal operations. What is lazy evaluation?
24. When is a plain `for` loop clearer than a stream? Give an example.

## 5. Spring and dependency injection
25. ★ What is Inversion of Control? What is dependency injection? Are they the same thing?
26. ★ Constructor injection vs field injection vs setter injection. Why does this project insist on constructor injection?
27. ★ What does Lombok's `@RequiredArgsConstructor` generate, and why does Spring not need `@Autowired` on it?
28. ★ `@Component` vs `@Service` vs `@Repository` vs `@Controller`: is there any functional difference?
29. ★ `@Component` vs `@Bean`: when must you use `@Bean`? (Use `Clock` from BE-012.)
30. ★ What is a `@Configuration` class? Why are they proxied with CGLIB?
31. What is the default bean scope? Name two other scopes and when you'd use them.
32. Describe the bean lifecycle at a high level. Where do `@PostConstruct` and `@PreDestroy` fit?
33. ★ Two beans of the same type exist. How does Spring decide which to inject? (`@Primary`, `@Qualifier`, parameter name.)
34. What does `@SpringBootApplication` combine? How does component scanning decide which packages to scan?
35. What is auto-configuration? How does Spring Boot know to configure a DataSource for H2?
36. ★ `@ConfigurationProperties` vs `@Value`: trade-offs.
37. ★ What is a Spring profile? How do you activate one, and how does `@Profile("!dev")` work?
38. What is a circular dependency? Why does constructor injection surface it immediately?

## 6. Spring MVC and REST
39. ★ Walk through what happens from an HTTP request arriving to your controller method running (DispatcherServlet, handler mapping, argument resolution, message conversion).
40. ★ `@PathVariable` vs `@RequestParam` vs `@RequestBody`: when do you use each?
41. `@Controller` vs `@RestController`.
42. ★ `ResponseEntity` vs `@ResponseStatus`: when do you need `ResponseEntity`?
43. ★ How does `@RestControllerAdvice` work? What happens to an exception no handler catches?
44. ★ Which status code for: created, deleted with no body, validation failed, not found, duplicate email, course full, server bug, not implemented?
45. ★ PUT vs PATCH vs POST. Which are idempotent? Is DELETE idempotent if the second call returns 404?
46. What is CORS? Who enforces it, the browser or the server? Why doesn't curl care?
47. Why use DTOs instead of returning entities from controllers? Name at least three reasons.

## 7. Validation
48. ★ Where does Bean Validation run when you put `@Valid` on a `@RequestBody`? What exception is thrown when it fails?
49. ★ `@NotNull` vs `@NotEmpty` vs `@NotBlank`.
50. ★ Why doesn't validation cascade into nested objects by default?
51. ★ Where should "end time after start time" be validated, and why not in a single-field annotation?

## 8. JPA and Hibernate
52. JPA vs Hibernate vs Spring Data JPA: what is each?
53. ★ What is the persistence context? What does "managed", "detached" and "transient" mean?
54. ★ What is dirty checking? Why does `updateCapacity` (live bug L02) not persist?
55. ★ `@ManyToOne` vs `@OneToMany`: which side owns the relationship and why? What does `mappedBy` mean?
56. ★ `FetchType.LAZY` vs `EAGER`. Default for each relationship type? Why prefer LAZY?
57. ★ What is the N+1 problem? How do you detect it and how do you fix it? (BE-019)
58. ★ What is `LazyInitializationException` and what is open-session-in-view? Why do we set `open-in-view: false`?
59. How do Spring Data derived query methods work? What happens at startup if a method name refers to a non-existent property?
60. When would you write JPQL with `@Query` instead? JPQL vs native SQL?
61. What does `cascade = CascadeType.ALL` do, and why can it be dangerous on `@ManyToOne`?
62. `@GeneratedValue(strategy = IDENTITY)` vs `SEQUENCE`: practical differences?

## 9. Transactions
63. ★ What does `@Transactional` do under the hood? (Proxies, interceptors, commit/rollback.)
64. ★ On which exceptions does Spring roll back by default? How do you change that? (DBG-B03)
65. ★ Why doesn't `@Transactional` work on self-invocation or private methods?
66. What does `readOnly = true` do and not do?
67. What is propagation? Explain `REQUIRED` vs `REQUIRES_NEW` with an example.
68. ★ ACID: explain each letter. What isolation level problems exist (dirty read, non-repeatable read, phantom)?
69. ★ Two users take the last seat in a course at the same moment. What goes wrong, and how do you prevent it?
70. Should transactions live in the controller, service, or repository layer? Why?

## 10. SQL and databases
71. INNER JOIN vs LEFT JOIN. Which join does an optional `@ManyToOne` (announcement to course) need when listing announcements?
72. What is an index? What are the costs? Which columns in this schema deserve one?
73. What does a unique constraint give you that an `existsByEmail` check does not?
74. ★ Write SQL for "number of active enrollments per course, including courses with zero".
75. What is normalisation? Why is `availablePlaces` computed instead of stored?

## 11. Testing
76. ★ Unit test vs slice test vs full integration test. Which annotation gives each, and what does each load?
77. ★ `@MockitoBean` vs Mockito's `@Mock`: when do you use which?
78. ★ Why is `BatchEnrollmentRollbackTest` deliberately not `@Transactional`?
79. What makes a test flaky? How does an injected `Clock` help?

## 12. React
80. ★ What is a component? What are props, and why are they read-only?
81. ★ What is state? Why must you never mutate state directly? (Live bug L05.)
82. ★ What triggers a re-render? What does React compare?
83. ★ Explain `useEffect`: when does it run, what is the dependency array, and what is the cleanup function for?
84. ★ What is a stale closure? (DBG-F01.)
85. ★ `setCount(count + 1)` vs `setCount(c => c + 1)`.
86. ★ Controlled vs uncontrolled inputs.
87. ★ Why do lists need `key`? Why is the array index a bad key when items can be reordered? (DBG-F03.)
88. ★ What does `fetch` do on a 404? How do you handle errors properly?
89. What is lifting state up? When does a component own state vs receive it via props?
90. What is derived state and why should you avoid storing it? (FE-012.)
91. Why does React StrictMode run effects twice in development?
92. How does React Router's `useParams` work, and why might a page not refetch when only the parameter changes?

## 13. Debugging technique
93. You get a 500 from an endpoint. Walk through your first five minutes.
94. Frontend shows wrong data but the API response looks right. Where do you look?
95. How do you read a long Spring stack trace efficiently?
96. How would you prove whether a bug is in the frontend or the backend?
97. How do you debug "it works on my machine"?

## 14. Lombok
98. ★ Why do the entities combine `@Builder`, `@AllArgsConstructor(access = PRIVATE)` and `@NoArgsConstructor(access = PROTECTED)`? What breaks if you remove each one?
99. ★ What does `@Builder.Default` fix? What value would `Student.builder().build().isActive()` return without it?
100. Why is `@Data` a poor choice on a JPA entity?
