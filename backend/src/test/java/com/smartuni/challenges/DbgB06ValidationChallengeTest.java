package com.smartuni.challenges;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * DBG-B06: An event with a garbage organiser email was accepted. The controller has @Valid. The DTO has @Email.
 * So why did validation not run on the email?
 */
@Tag("challenge")
@Tag("DBG-B06")
class DbgB06ValidationChallengeTest {

    record Organiser(@NotBlank String name, @Email @NotBlank String email) {
    }

    record CampusEventRequest(@NotBlank String title, @NotNull Organiser organiser) {
    }

    @RestController
    @RequestMapping("/challenge/events")
    static class CampusEventController {

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public String create(@Valid @RequestBody CampusEventRequest request) {
            return request.title();
        }
    }

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CampusEventController()).build();
    }

    @Test
    void rejectsInvalidOrganiserEmail() throws Exception {
        mockMvc.perform(post("/challenge/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Hackathon", "organiser": {"name": "Calvin", "email": "definitely-not-email"}}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void acceptsValidEvent() throws Exception {
        mockMvc.perform(post("/challenge/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Hackathon", "organiser": {"name": "Calvin", "email": "calvin@smartuni.ac.zw"}}
                                """))
                .andExpect(status().isCreated());
    }
}
