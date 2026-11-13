package com.smartuni.booking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Counts SQL statements with Hibernate statistics. The seeded bookings use 4 venues and 4 courses,
 * so the naive implementation runs 1 + 4 + 4 = 9 statements. Target: 1.
 */
@SpringBootTest(properties = "spring.jpa.properties.hibernate.generate_statistics=true")
@AutoConfigureMockMvc
@Tag("todo")
@Tag("BE-019")
class BookingListQueryCountTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void listingBookingsUsesASingleQuery() throws Exception {
        Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        statistics.clear();

        mockMvc.perform(get("/api/bookings")).andExpect(status().isOk());

        assertThat(statistics.getPrepareStatementCount())
                .as("SQL statements executed for GET /api/bookings")
                .isEqualTo(1);
    }
}
