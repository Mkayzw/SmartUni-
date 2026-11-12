package com.smartuni.booking;

import java.time.LocalDateTime;

/**
 * TODO-BE-014: add validation annotations. Every field is required; attendance must be at least 1.
 * Cross-field rules (end after start, in the future, within opening hours) belong in the service.
 * Why can't a single-field annotation check "end after start"? (Bonus: a class-level custom constraint can.)
 */
public record CreateBookingRequest(
        Long venueId,
        Long courseId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer expectedAttendance
) {
}
