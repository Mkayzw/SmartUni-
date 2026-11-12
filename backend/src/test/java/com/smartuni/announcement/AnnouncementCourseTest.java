package com.smartuni.announcement;

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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Tag("todo")
@Tag("BE-017")
class AnnouncementCourseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsCourseAnnouncement() throws Exception {
        mockMvc.perform(post("/api/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Lecture moved", "content": "Now in E001", "courseId": 2}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courseId").value(2))
                .andExpect(jsonPath("$.courseCode").value("CS201"));
    }

    @Test
    void campusWideAnnouncementHasNoCourse() throws Exception {
        mockMvc.perform(post("/api/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Campus closed", "content": "Public holiday"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courseId").isEmpty())
                .andExpect(jsonPath("$.courseCode").isEmpty());
    }

    @Test
    void unknownCourseReturns404() throws Exception {
        mockMvc.perform(post("/api/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Ghost", "content": "No such course", "courseId": 999999}
                                """))
                .andExpect(status().isNotFound());
    }
}
