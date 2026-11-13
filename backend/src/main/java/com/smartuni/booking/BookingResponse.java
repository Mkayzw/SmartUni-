package com.smartuni.booking;

import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long venueId,
        String venueName,
        Long courseId,
        String courseCode,
        LocalDateTime startTime,
        LocalDateTime endTime,
        int expectedAttendance
) {
}
