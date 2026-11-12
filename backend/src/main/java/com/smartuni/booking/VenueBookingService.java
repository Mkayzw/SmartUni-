package com.smartuni.booking;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VenueBookingService {

    private final VenueBookingRepository venueBookingRepository;

    /**
     * Works, but look at the SQL log when you call GET /api/bookings. (TODO-BE-019)
     */
    @Transactional(readOnly = true)
    public List<BookingResponse> listBookings() {
        return venueBookingRepository.findAllByOrderByStartTimeAsc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> listBookingsForVenue(Long venueId) {
        return venueBookingRepository.findAllByVenueIdOrderByStartTimeAsc(venueId).stream()
                .map(BookingResponse::from)
                .toList();
    }

    /**
     * TODO-BE-014 + TODO-BE-015: Book a venue for a course.
     * Rules:
     *   - venue and course must exist                                   -> 404
     *   - endTime must be after startTime                               -> 409
     *   - startTime must be in the future (use the injected Clock, TODO-BE-012) -> 409
     *   - duration within [minDurationMinutes, maxDurationMinutes]      -> 409   (BookingPolicyProperties, TODO-BE-013)
     *   - start >= openingTime and end <= closingTime, same day         -> 409
     *   - expectedAttendance <= venue capacity                          -> 409
     *   - no overlapping booking for the same venue (TODO-BE-015)       -> 409
     *
     * Build the new entity with VenueBooking.builder() (project convention).
     *
     * Test: VenueBookingRulesTest
     */
    public BookingResponse createBooking(CreateBookingRequest request) {
        throw new UnsupportedOperationException("TODO-BE-014: implement venue booking");
    }

    private BookingResponse toResponse(VenueBooking booking) {
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
