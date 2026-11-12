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

    public static BookingResponse from(VenueBooking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getVenue().getId(),
                booking.getVenue().getName(),
                booking.getCourse().getId(),
                booking.getCourse().getCode(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getExpectedAttendance());
    }
}
