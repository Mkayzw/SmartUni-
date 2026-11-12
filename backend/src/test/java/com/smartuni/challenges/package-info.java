/**
 * Debugging challenges. Every class here contains at least one deliberate bug.
 * See DEBUGGING_CHALLENGES.md in the repository root for the symptoms and progressive hints.
 *
 * Isolation rules (so the bugs never break the real application or the baseline tests):
 *   - every buggy class is a NESTED class inside its test class. Spring Boot's TestTypeExcludeFilter
 *     skips classes nested in test classes during component scanning, so a @RestController here is never
 *     registered in the real application context or in other tests;
 *   - beans are declared in nested @TestConfiguration classes;
 *   - controllers are tested with MockMvcBuilders.standaloneSetup(...).
 * The one exception is ChallengeVenueRepository (DBG-B05): Spring Data registers it everywhere, which is harmless
 * because its query is valid JPQL, just wrong.
 * Keep those rules when you fix them: fix the bug, not the isolation.
 */
package com.smartuni.challenges;
