package com.smartuni.challenges;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;

/** DBG-B01: The timetable endpoints don't respond the way the frontend expects. Tests describe the contract. */
@Tag("challenge")
@Tag("DBG-B01")
class DbgB01MappingChallengeTest {

    record TimetableEntryRequest(String courseCode, String day) {
    }

    @RestController
    @RequestMapping("/challenge/timetable")
    static class TimetableController {

        @GetMapping("/{courseCode}")
        public String timetableForCourse(@RequestParam String courseCode) {
            return "Timetable for " + courseCode;
        }

        @RequestMapping(value = "/entries", method = RequestMethod.GET)
        @ResponseStatus(HttpStatus.CREATED)
        public String addEntry(@RequestBody TimetableEntryRequest request) {
            return request.courseCode() + " on " + request.day();
        }
    }

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TimetableController()).build();
    }

    @Test
    void getsTimetableByCourseCodeInThePath() throws Exception {
        mockMvc.perform(get("/challenge/timetable/CS101"))
                .andExpect(status().isOk())
                .andExpect(content().string("Timetable for CS101"));
    }

    @Test
    void addsEntryWithPost() throws Exception {
        mockMvc.perform(post("/challenge/timetable/entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"courseCode\": \"CS201\", \"day\": \"MONDAY\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("CS201 on MONDAY"));
    }
}
