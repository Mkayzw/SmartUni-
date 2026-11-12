package com.smartuni.booking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Proves the booking service asks the INJECTED Clock for "now" rather than calling LocalDateTime.now().
 * The test replaces the clock with one frozen in 2032, so a 2031 booking is in the past.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Tag("todo")
@Tag("BE-012")
class BookingUsesInjectedClockTest {

    @TestConfiguration
    static class FrozenClockConfig {

        @Bean
        @Primary
        Clock frozenClock() {
            return Clock.fixed(Instant.parse("2032-01-01T00:00:00Z"), ZoneOffset.UTC);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void bookingBeforeTheFrozenNowIsRejected() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"venueId": 2, "courseId": 3, "startTime": "2031-06-01T09:00:00",
                                 "endTime": "2031-06-01T11:00:00", "expectedAttendance": 10}
                                """))
                .andExpect(status().isConflict());
    }
}
