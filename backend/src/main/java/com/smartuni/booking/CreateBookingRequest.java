package com.smartuni.booking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateBookingRequest(
        @NotNull
        Long venueId,

        @NotNull
        Long courseId,

        @NotNull
        LocalDateTime startTime,

        @NotNull
        LocalDateTime endTime,

        @NotNull
        @Min(1)
        Integer expectedAttendance
) {}