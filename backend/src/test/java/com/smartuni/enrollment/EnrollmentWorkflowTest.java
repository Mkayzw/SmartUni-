package com.smartuni.enrollment;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seed data reminder (data.sql):
 *   CS101 (id 1) capacity 3, FULL with students 1, 2, 3
 *   CS201 (id 2) students 1 and 7       MATH110 (id 4) student 7       SE350 (id 5) student 7 CANCELLED
 *   student 8 is inactive
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EnrollmentWorkflowTest {

    @Autowired
    private MockMvc mockMvc;

    private String enrollmentJson(long studentId, long courseId) {
        return "{\"studentId\": " + studentId + ", \"courseId\": " + courseId + "}";
    }

    // ---------- baseline ----------

    @Test
    void missingCourseIdIsRejectedBeforeReachingTheService() throws Exception {
        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentId\": 5}"))
                .andExpect(status().isBadRequest());
    }

    // ---------- TODO-BE-009 ----------

    @Test
    @Tag("todo")
    @Tag("BE-009")
    void enrollsActiveStudentInCourseWithSpace() throws Exception {
        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson(5, 3)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value(5))
                .andExpect(jsonPath("$.studentName").value("Tafadzwa Sibanda"))
                .andExpect(jsonPath("$.courseCode").value("CS301"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.enrolledAt").exists());
    }

    @Test
    @Tag("todo")
    @Tag("BE-009")
    void cannotEnrollSameStudentTwice() throws Exception {
        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson(1, 2)))
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("todo")
    @Tag("BE-009")
    void cannotExceedCourseCapacity() throws Exception {
        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson(4, 1)))
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("todo")
    @Tag("BE-009")
    void inactiveStudentCannotEnroll() throws Exception {
        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson(8, 3)))
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("todo")
    @Tag("BE-009")
    void cancelledEnrollmentDoesNotCountAsDuplicate() throws Exception {
        // Student 7 has a CANCELLED enrollment in SE350 (id 5)
        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson(7, 5)))
                .andExpect(status().isCreated());
    }

    @Test
    @Tag("todo")
    @Tag("BE-009")
    void unknownStudentOrCourseReturns404() throws Exception {
        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson(999999, 3)))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson(5, 999999)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Tag("todo")
    @Tag("BE-009")
    void listsEnrollments() throws Exception {
        mockMvc.perform(get("/api/enrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(7)))
                .andExpect(jsonPath("$[0].courseCode").exists());
    }

    // ---------- TODO-BE-010 (requires BE-009) ----------

    @Test
    @Tag("todo")
    @Tag("BE-010")
    void cancelsEnrollmentAndAllowsReEnrollment() throws Exception {
        String body = mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson(6, 4)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Integer enrollmentId = JsonPath.read(body, "$.id");

        mockMvc.perform(patch("/api/enrollments/" + enrollmentId + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        mockMvc.perform(patch("/api/enrollments/" + enrollmentId + "/cancel"))
                .andExpect(status().isConflict());

        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollmentJson(6, 4)))
                .andExpect(status().isCreated());
    }

    @Test
    @Tag("todo")
    @Tag("BE-010")
    void cancellingUnknownEnrollmentReturns404() throws Exception {
        mockMvc.perform(patch("/api/enrollments/999999/cancel"))
                .andExpect(status().isNotFound());
    }

    // ---------- TODO-BE-011 ----------

    @Test
    @Tag("todo")
    @Tag("BE-011")
    void returnsOnlyActiveCoursesForStudent() throws Exception {
        mockMvc.perform(get("/api/students/7/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].code", containsInAnyOrder("CS201", "MATH110")));
    }

    @Test
    @Tag("todo")
    @Tag("BE-011")
    void coursesForUnknownStudentReturns404() throws Exception {
        mockMvc.perform(get("/api/students/999999/courses"))
                .andExpect(status().isNotFound());
    }
}
