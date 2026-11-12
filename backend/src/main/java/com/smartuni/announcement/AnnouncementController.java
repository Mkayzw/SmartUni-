package com.smartuni.announcement;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping
    public List<AnnouncementResponse> listAnnouncements() {
        return announcementService.listAnnouncements();
    }

    @PostMapping
    public ResponseEntity<AnnouncementResponse> createAnnouncement(@Valid @RequestBody CreateAnnouncementRequest request) {
        AnnouncementResponse createdAnnouncement = announcementService.createAnnouncement(request);
        return ResponseEntity.created(URI.create("/api/announcements/" + createdAnnouncement.id())).body(createdAnnouncement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.noContent().build();
    }

    // TODO-BE-016: PATCH /api/announcements/{id}/pin with JSON body {"pinned": true}
    //   Create a small request record for the body. Return the updated announcement.
}
