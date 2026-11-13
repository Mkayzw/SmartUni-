package com.smartuni.announcement;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Autowired
    private AnnouncementRepository announcementRepository;

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
    void putReplacesEditableFieldsAndPreservesCreationTime() throws Exception {
        var createdAt = announcementRepository.findById(2L).orElseThrow().getCreatedAt();

        mockMvc.perform(put("/api/announcements/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": " Updated title ", "content": "Updated body", "pinned": true, "courseId": 2}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Updated title"))
                .andExpect(jsonPath("$.pinned").value(true))
                .andExpect(jsonPath("$.courseCode").value("CS201"));

        // PUT replaces the representation: omitted courseId clears it, omitted pinned becomes false.
        mockMvc.perform(put("/api/announcements/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Campus news", "content": "For everyone"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").isEmpty())
                .andExpect(jsonPath("$.courseCode").isEmpty())
                .andExpect(jsonPath("$.pinned").value(false));

        var saved = announcementRepository.findById(2L).orElseThrow();
        org.assertj.core.api.Assertions.assertThat(saved.getTitle()).isEqualTo("Campus news");
        org.assertj.core.api.Assertions.assertThat(saved.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void puttingUnknownAnnouncementReturns404() throws Exception {
        mockMvc.perform(put("/api/announcements/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "News", "content": "Body"}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void putRequiresTitleAndContent() throws Exception {
        mockMvc.perform(put("/api/announcements/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "News"}
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
