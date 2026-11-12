package com.smartuni.announcement;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Example of a SLICE test. @WebMvcTest starts only the web layer (controllers, advice, converters).
 * No JPA, no database, no services: the service is replaced with a Mockito mock.
 * Fast, and it tests exactly one thing: the HTTP contract of the controller.
 */
@WebMvcTest(AnnouncementController.class)
class AnnouncementControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnnouncementService announcementService;

    @Test
    void returnsWhateverTheServiceReturns() throws Exception {
        AnnouncementResponse response = new AnnouncementResponse(42L, "Title", "Body", true, LocalDateTime.of(2026, 1, 1, 9, 0));
        when(announcementService.listAnnouncements()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/announcements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(42))
                .andExpect(jsonPath("$[0].pinned").value(true));
    }
}
