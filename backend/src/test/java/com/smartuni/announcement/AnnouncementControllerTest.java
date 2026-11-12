package com.smartuni.announcement;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

/**
 * Full-stack test: real controllers, services, repositories and H2.
 * @Transactional on the test class rolls back after each test, so tests don't leak data into each other.
 * (MockMvc runs on the test thread, so the request joins the test's transaction.)
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AnnouncementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listsSeededAnnouncements() throws Exception {
        mockMvc.perform(get("/api/announcements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(3)));
    }

    @Test
    void createsAnnouncement() throws Exception {
        mockMvc.perform(post("/api/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Exam timetable", "content": "Published on the portal."}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.pinned").value(false));
    }

    @Test
    void rejectsBlankTitle() throws Exception {
        mockMvc.perform(post("/api/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "   ", "content": "Body"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("todo")
    @Tag("BE-005")
    void deletesExistingAnnouncement() throws Exception {
        mockMvc.perform(delete("/api/announcements/2"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/announcements"))
                .andExpect(jsonPath("$[?(@.id == 2)]").isEmpty());
    }

    @Test
    @Tag("todo")
    @Tag("BE-005")
    void deletingUnknownAnnouncementReturns404() throws Exception {
        mockMvc.perform(delete("/api/announcements/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}
