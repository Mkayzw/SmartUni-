package com.smartuni.announcement;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Tag("todo")
@Tag("BE-016")
class AnnouncementPinTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void pinsAnnouncement() throws Exception {
        mockMvc.perform(patch("/api/announcements/3/pin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"pinned\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.pinned").value(true));
    }

    @Test
    void unpinsAnnouncement() throws Exception {
        mockMvc.perform(patch("/api/announcements/1/pin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"pinned\": false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pinned").value(false));
    }

    @Test
    void listShowsPinnedFirstThenNewest() throws Exception {
        // Seed: 1 pinned (Aug 1), 2 not pinned (Aug 5), 3 not pinned (Aug 7)
        mockMvc.perform(patch("/api/announcements/3/pin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"pinned\": true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/announcements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[1].id").value(1))
                .andExpect(jsonPath("$[2].id").value(2));
    }

    @Test
    void pinningUnknownAnnouncementReturns404() throws Exception {
        mockMvc.perform(patch("/api/announcements/999999/pin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"pinned\": true}"))
                .andExpect(status().isNotFound());
    }
}
