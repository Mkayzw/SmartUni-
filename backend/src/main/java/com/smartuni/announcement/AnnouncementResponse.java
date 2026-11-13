package com.smartuni.announcement;

import java.time.LocalDateTime;

/** TODO-BE-017: include courseId and courseCode (both null for campus-wide announcements). */
public record AnnouncementResponse(
        Long id,
        Long courseId,
        String courseCode,
        String title,
        String content,
        boolean pinned,
        LocalDateTime createdAt

) {
}
