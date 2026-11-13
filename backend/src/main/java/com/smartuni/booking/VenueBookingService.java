package com.smartuni.booking;

import com.smartuni.common.BusinessRuleViolationException;
import com.smartuni.common.ResourceNotFoundException;
import com.smartuni.config.BookingPolicyProperties;
import com.smartuni.course.Course;
import com.smartuni.course.CourseService;
import com.smartuni.venue.Venue;
import com.smartuni.venue.VenueService;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VenueBookingService {

    private final VenueBookingRepository venueBookingRepository;
    private final VenueService venueService;
    private final CourseService courseService;
    private final BookingPolicyProperties bookingPolicyProperties;
    private final Clock clock;
    /**
     *
     * Works, but look at the SQL log when you call GET /api/bookings. (TODO-BE-019)
     */



    public VenueBooking findById(Long id) {
        return venueBookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", id));
    }



    @Transactional(readOnly = true)
    public Page<BookingResponse> listBookings(Pageable pageable) {
        return venueBookingRepository.findAll(pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<BookingResponse> listBookingsForVenue(Long venueId, Pageable pageable) {
        return venueBookingRepository.findAllByVenueId(venueId, pageable)
                .map(this::toResponse);
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
     *
     *
     * Test: VenueBookingRulesTest
     */
    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request) {
        Venue venue = venueService.findById(request.venueId());
        Course course = courseService.findById(request.courseId());

        if (!request.endTime().isAfter(request.startTime())) {
            throw new BusinessRuleViolationException("End time must be after start time");
        }

        if (!request.startTime().isAfter(LocalDateTime.now(clock))) {
            throw new BusinessRuleViolationException("Start time must be in the future");
        }

        Duration duration = Duration.between(request.startTime(), request.endTime());
        Duration minimum = Duration.ofMinutes(bookingPolicyProperties.minDurationMinutes());
        Duration maximum = Duration.ofMinutes(bookingPolicyProperties.maxDurationMinutes());

        if (duration.compareTo(minimum) < 0 || duration.compareTo(maximum) > 0) {
            throw new BusinessRuleViolationException("Booking duration is outside the allowed limits");
        }

        if (!request.startTime().toLocalDate().equals(request.endTime().toLocalDate())) {
            throw new BusinessRuleViolationException("Booking must start and end on the same day");
        }

        if (request.startTime().toLocalTime().isBefore(bookingPolicyProperties.openingTime())
                || request.endTime().toLocalTime().isAfter(bookingPolicyProperties.closingTime())) {
            throw new BusinessRuleViolationException("Booking must be within opening hours");
        }

        if (request.expectedAttendance() > venue.getCapacity()) {
            throw new BusinessRuleViolationException("Expected attendance exceeds venue capacity");
        }

        if (venueBookingRepository.existsOverlappingBooking(
                request.venueId(), request.startTime(), request.endTime())) {
            throw new BusinessRuleViolationException("Venue already has a booking during this time");
        }

        VenueBooking booking = VenueBooking.builder()
                .venue(venue)
                .course(course)
                .startTime(request.startTime())
                .endTime(request.endTime())
                .expectedAttendance(request.expectedAttendance())
                .build();

        VenueBooking savedBooking = venueBookingRepository.save(booking);
        return toResponse(savedBooking);
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
