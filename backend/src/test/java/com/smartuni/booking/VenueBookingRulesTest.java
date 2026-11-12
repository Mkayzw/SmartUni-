package com.smartuni.booking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seed data reminder:
 *   venue 1 Main Lecture Theatre (200)   venue 2 Computer Lab 1 (35)   venue 3 Seminar Room (20)   venue 4 Science Hall (120)
 *   existing booking: venue 1 on 2030-02-03 08:00-10:00
 * Policy (application.yml): 30-240 minutes, between 07:00 and 21:00.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VenueBookingRulesTest {

    @Autowired
    private MockMvc mockMvc;

    private ResultActions book(long venueId, String start, String end, int attendance) throws Exception {
        String json = """
                {"venueId": %d, "courseId": 3, "startTime": "%s", "endTime": "%s", "expectedAttendance": %d}
                """.formatted(venueId, start, end, attendance);
        return mockMvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(json));
    }

    // ---------- TODO-BE-014 ----------

    @Test
    @Tag("todo")
    @Tag("BE-014")
    void createsValidBooking() throws Exception {
        book(2, "2031-03-03T09:00:00", "2031-03-03T11:00:00", 30)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.venueName").value("Computer Lab 1"))
                .andExpect(jsonPath("$.courseCode").value("CS301"));
    }

    @Test
    @Tag("todo")
    @Tag("BE-014")
    void rejectsAttendanceAboveVenueCapacity() throws Exception {
        book(2, "2031-03-03T09:00:00", "2031-03-03T11:00:00", 36)
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("todo")
    @Tag("BE-014")
    void acceptsAttendanceExactlyAtCapacity() throws Exception {
        book(3, "2031-03-03T09:00:00", "2031-03-03T11:00:00", 20)
                .andExpect(status().isCreated());
    }

    @Test
    @Tag("todo")
    @Tag("BE-014")
    void rejectsEndBeforeStart() throws Exception {
        book(2, "2031-03-03T11:00:00", "2031-03-03T09:00:00", 10)
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("todo")
    @Tag("BE-014")
    void rejectsBookingInThePast() throws Exception {
        book(2, "2020-03-03T09:00:00", "2020-03-03T11:00:00", 10)
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("todo")
    @Tag("BE-014")
    void rejectsDurationOutsidePolicy() throws Exception {
        book(2, "2031-03-03T09:00:00", "2031-03-03T09:15:00", 10)
                .andExpect(status().isConflict());
        book(2, "2031-03-03T09:00:00", "2031-03-03T14:00:00", 10)
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("todo")
    @Tag("BE-014")
    void rejectsBookingOutsideOpeningHours() throws Exception {
        book(2, "2031-03-03T06:00:00", "2031-03-03T08:00:00", 10)
                .andExpect(status().isConflict());
        book(2, "2031-03-03T20:00:00", "2031-03-03T22:00:00", 10)
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("todo")
    @Tag("BE-014")
    void unknownVenueReturns404() throws Exception {
        book(999999, "2031-03-03T09:00:00", "2031-03-03T11:00:00", 10)
                .andExpect(status().isNotFound());
    }

    @Test
    @Tag("todo")
    @Tag("BE-014")
    void missingFieldsAreRejectedWith400() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"courseId\": 3}"))
                .andExpect(status().isBadRequest());
    }

    // ---------- TODO-BE-015: overlap with venue 1, 2030-02-03 08:00-10:00 ----------

    @Test
    @Tag("todo")
    @Tag("BE-015")
    void rejectsPartialOverlap() throws Exception {
        book(1, "2030-02-03T09:00:00", "2030-02-03T11:00:00", 50)
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("todo")
    @Tag("BE-015")
    void rejectsBookingThatContainsExistingOne() throws Exception {
        book(1, "2030-02-03T07:30:00", "2030-02-03T10:30:00", 50)
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("todo")
    @Tag("BE-015")
    void rejectsBookingInsideExistingOne() throws Exception {
        book(1, "2030-02-03T08:30:00", "2030-02-03T09:30:00", 50)
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("todo")
    @Tag("BE-015")
    void allowsBackToBackBookings() throws Exception {
        book(1, "2030-02-03T10:00:00", "2030-02-03T12:00:00", 50)
                .andExpect(status().isCreated());
    }

    @Test
    @Tag("todo")
    @Tag("BE-015")
    void sameTimeInDifferentVenueIsFine() throws Exception {
        book(4, "2030-02-03T08:00:00", "2030-02-03T10:00:00", 50)
                .andExpect(status().isCreated());
    }
}
