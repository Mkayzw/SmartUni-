package com.smartuni.venue;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Tag("todo")
@Tag("BE-020")
class AvailableVenuesTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void excludesBusyAndTooSmallVenues() throws Exception {
        // 2030-02-03 09:00-11:00: venue 1 busy 08-10, venue 2 busy 10-12, venue 3 too small (20 < 30)
        mockMvc.perform(get("/api/venues/available")
                        .param("start", "2030-02-03T09:00:00")
                        .param("end", "2030-02-03T11:00:00")
                        .param("minCapacity", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", contains(4)));
    }

    @Test
    void rejectsInvertedRange() throws Exception {
        mockMvc.perform(get("/api/venues/available")
                        .param("start", "2030-02-03T11:00:00")
                        .param("end", "2030-02-03T09:00:00"))
                .andExpect(status().isConflict());
    }
}
