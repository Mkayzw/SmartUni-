package com.smartuni.announcement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** TODO-BE-017: accept an optional courseId. */
public record CreateAnnouncementRequest(
        @NotBlank @Size(max = 150) String title,
        @NotBlank @Size(max = 2000) String content
) {
}
