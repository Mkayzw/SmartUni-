package com.smartuni.config;

import org.springframework.context.annotation.Configuration;

/**
 * TODO-BE-012: Expose a java.time.Clock as a Spring bean.
 *
 * WHY: code that calls LocalDateTime.now() directly is impossible to test deterministically.
 * If the "current time" is an injected dependency, a test can supply a fixed Clock.
 *
 * Right now this is an empty configuration class. The application starts fine without a Clock bean
 * because nothing injects one yet. Your job:
 *   1. Add a @Bean method that returns Clock.systemDefaultZone().
 *   2. Read the "component scan vs explicit @Bean" note in TASKS.md and be ready to explain
 *      why Clock cannot be a @Component.
 *   3. Inject it into VenueBookingService (constructor injection) as part of TODO-BE-014.
 *
 * Test: ClockConfigTest
 */
@Configuration
public class ClockConfig {

}
