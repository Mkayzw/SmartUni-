package com.smartuni.enrollment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * NOT @Transactional on purpose.
 * If the test itself ran inside a transaction, everything would be rolled back at the end anyway and
 * we could not tell whether YOUR transaction boundary rolled back. Here the request commits or rolls back for real.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Tag("todo")
@Tag("BE-018")
class BatchEnrollmentRollbackTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    void failingCourseRollsBackTheWholeBatch() throws Exception {
        long activeBefore = enrollmentRepository.countByStatus(EnrollmentStatus.ACTIVE);

        // Student 4: CS201 (id 2) has space, CS101 (id 1) is full -> whole request must fail
        mockMvc.perform(post("/api/enrollments/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentId\": 4, \"courseIds\": [2, 1]}"))
                .andExpect(status().isConflict());

        long activeAfter = enrollmentRepository.countByStatus(EnrollmentStatus.ACTIVE);
        assertThat(activeAfter)
                .as("the CS201 enrollment saved before CS101 failed must have been rolled back")
                .isEqualTo(activeBefore);
    }
}
