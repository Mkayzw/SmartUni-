package com.smartuni.challenges;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DBG-B07: The frontend shows an empty card for rooms that don't exist, and "Deleted!" for rooms that were never there.
 */
@Tag("challenge")
@Tag("DBG-B07")
class DbgB07HttpStatusChallengeTest {

    @RestController
    @RequestMapping("/challenge/rooms")
    static class RoomController {

        private final Map<Long, String> rooms = new HashMap<>(Map.of(1L, "E001", 2L, "L210"));

        @GetMapping("/{id}")
        public ResponseEntity<String> getRoom(@PathVariable Long id) {
            return ResponseEntity.ok(rooms.get(id));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
            rooms.remove(id);
            return ResponseEntity.ok("Deleted");
        }
    }

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RoomController()).build();
    }

    @Test
    void unknownRoomIs404() throws Exception {
        mockMvc.perform(get("/challenge/rooms/99")).andExpect(status().isNotFound());
    }

    @Test
    void deletingExistingRoomIs204WithNoBody() throws Exception {
        mockMvc.perform(delete("/challenge/rooms/1")).andExpect(status().isNoContent());
    }

    @Test
    void deletingUnknownRoomIs404() throws Exception {
        mockMvc.perform(delete("/challenge/rooms/99")).andExpect(status().isNotFound());
    }
}
