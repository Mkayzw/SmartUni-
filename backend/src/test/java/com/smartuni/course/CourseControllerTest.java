package com.smartuni.course;

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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listsCoursesOrderedByCode() throws Exception {
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("CS101"));
    }

    @Test
    void createsCourse() throws Exception {
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"code": "CS410", "title": "Distributed Systems", "capacity": 25}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("CS410"));
    }

    @Test
    void rejectsInvalidCourseCode() throws Exception {
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"code": "lowercase", "title": "Bad", "capacity": 25}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("todo")
    @Tag("BE-006")
    void fullCourseHasNoAvailablePlaces() throws Exception {
        // CS101: capacity 3, three ACTIVE enrollments in data.sql
        mockMvc.perform(get("/api/courses"))
                .andExpect(jsonPath("$[?(@.code == 'CS101')].availablePlaces").value(0));
    }

    @Test
    @Tag("todo")
    @Tag("BE-006")
    void cancelledEnrollmentsDoNotTakeAPlace() throws Exception {
        // CS201: capacity 30, 2 active.   SE350: capacity 25, 1 CANCELLED only.
        mockMvc.perform(get("/api/courses"))
                .andExpect(jsonPath("$[?(@.code == 'CS201')].availablePlaces").value(28))
                .andExpect(jsonPath("$[?(@.code == 'SE350')].availablePlaces").value(25));
    }
}
