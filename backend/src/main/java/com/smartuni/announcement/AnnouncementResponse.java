package com.smartuni.announcement;

import java.time.LocalDateTime;

/** TODO-BE-017: include courseId and courseCode (both null for campus-wide announcements). */
public record AnnouncementResponse(Long id, String title, String content, boolean pinned, LocalDateTime createdAt) {

    public static AnnouncementResponse from(Announcement announcement) {
        return new AnnouncementResponse(
                announcement.getId(),
                announcement.getTitle(),
                announcement.getContent(),
                announcement.isPinned(),
                announcement.getCreatedAt());
    }
}
