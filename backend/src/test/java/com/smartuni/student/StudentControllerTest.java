package com.smartuni.student;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.matchesRegex;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ---------- baseline (already passing) ----------

    @Test
    void listsStudents() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(8)));
    }

    @Test
    void getsStudentById() throws Exception {
        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("tendai.moyo@smartuni.ac.zw"));
    }

    @Test
    void unknownStudentReturns404() throws Exception {
        mockMvc.perform(get("/api/students/999999"))
                .andExpect(status().isNotFound());
    }

    // ---------- TODO-BE-001 ----------

    @Test
    @Tag("todo")
    @Tag("BE-001")
    void registersStudent() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName": "Ada", "lastName": "Lovelace", "email": "ada@test.smartuni.ac.zw"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesRegex(".*/api/students/\\d+")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @Tag("todo")
    @Tag("BE-001")
    void duplicateEmailIsRejectedWith409() throws Exception {
        // Also needs TODO-BE-003 (mapping BusinessRuleViolationException to an HTTP status).
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName": "Copy", "lastName": "Cat", "email": "tendai.moyo@smartuni.ac.zw"}
                                """))
                .andExpect(status().isConflict());
    }

    // ---------- TODO-BE-002 ----------

    @Test
    @Tag("todo")
    @Tag("BE-002")
    void rejectsInvalidEmail() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName": "Bad", "lastName": "Email", "email": "not-an-email"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("todo")
    @Tag("BE-002")
    void rejectsBlankNamesAndMissingEmail() throws Exception {
        // Also needs TODO-BE-003 for the fieldErrors body.
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName": "", "lastName": "  "}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.firstName").exists())
                .andExpect(jsonPath("$.fieldErrors.lastName").exists())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    // ---------- TODO-BE-004 ----------

    @Test
    @Tag("todo")
    @Tag("BE-004")
    void searchesByLastNameCaseInsensitively() throws Exception {
        mockMvc.perform(get("/api/students/search").param("name", "MOYO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @Tag("todo")
    @Tag("BE-004")
    void searchMatchesFirstOrLastName() throws Exception {
        // "ch" matches Rutendo CHikore (last name) and CHipo Banda (first name). Emails must NOT be searched.
        mockMvc.perform(get("/api/students/search").param("name", "ch"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(2, 7)));
    }

    // ---------- TODO-BE-007 ----------

    @Test
    @Tag("todo")
    @Tag("BE-007")
    void deactivatesAndReactivatesStudent() throws Exception {
        mockMvc.perform(patch("/api/students/3/status").param("active", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));

        mockMvc.perform(get("/api/students/3"))
                .andExpect(jsonPath("$.active").value(false));

        mockMvc.perform(patch("/api/students/3/status").param("active", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @Tag("todo")
    @Tag("BE-007")
    void changingStatusOfUnknownStudentReturns404() throws Exception {
        mockMvc.perform(patch("/api/students/999999/status").param("active", "false"))
                .andExpect(status().isNotFound());
    }
}
