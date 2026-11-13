package com.smartuni.announcement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnnouncementRequest(
        Long courseId,
        @NotBlank @Size(max = 150) String title,
        @NotBlank @Size(max = 2000) String content,
        boolean pinned
){}

