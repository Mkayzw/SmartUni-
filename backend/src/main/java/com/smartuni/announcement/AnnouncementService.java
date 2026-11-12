package com.smartuni.announcement;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The finished reference feature: read it end to end before starting the TODOs.
 */
@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    @Transactional(readOnly = true)
    public List<AnnouncementResponse> listAnnouncements() {
        return announcementRepository.findAllByOrderByIdDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AnnouncementResponse createAnnouncement(CreateAnnouncementRequest request) {
        Announcement announcement = Announcement.builder()
                .title(request.title())
                .content(request.content())
                .pinned(false)
                .build();
        Announcement savedAnnouncement = announcementRepository.save(announcement);
        return toResponse(savedAnnouncement);
    }

    /**
     * TODO-BE-005: Delete an announcement. Unknown id must produce 404, not 200 and not 500.
     * Note: what does repository.deleteById(...) do in Spring Data JPA 3 when the id does not exist?
     */
    public void deleteAnnouncement(Long id) {
        throw new UnsupportedOperationException("TODO-BE-005: implement announcement deletion");
    }

    /**
     * TODO-BE-016: Pin or unpin. Unknown id -> 404.
     */
    public AnnouncementResponse setPinned(Long id, boolean pinned) {
        throw new UnsupportedOperationException("TODO-BE-016: implement pin/unpin");
    }

    private AnnouncementResponse toResponse(Announcement announcement) {
        return new AnnouncementResponse(
                announcement.getId(),
                announcement.getTitle(),
                announcement.getContent(),
                announcement.isPinned(),
                announcement.getCreatedAt());
    }
}
