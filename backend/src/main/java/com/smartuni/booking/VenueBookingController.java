package com.smartuni.booking;

import java.net.URI;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VenueBookingController {

    private final VenueBookingService venueBookingService;

    @GetMapping("/bookings")
    public Page<BookingResponse> listBookings(
            @PageableDefault(size = 10, sort = {"startTime", "id"}) Pageable pageable) {
        return venueBookingService.listBookings(pageable);
    }

    @GetMapping("/venues/{venueId}/bookings")
    public Page<BookingResponse> listBookingsForVenue(
            @PathVariable Long venueId,
            @PageableDefault(size = 10, sort = {"startTime", "id"}) Pageable pageable) {
        return venueBookingService.listBookingsForVenue(venueId, pageable);
    }

    // TODO-BE-014: validation is not triggered for this body yet.
    @PostMapping("/bookings")
    public ResponseEntity<BookingResponse> createBooking(@Valid  @RequestBody CreateBookingRequest request) {
        BookingResponse createdBooking = venueBookingService.createBooking(request);
        return ResponseEntity.created(URI.create("/api/bookings/" + createdBooking.id())).body(createdBooking);
    }
}
