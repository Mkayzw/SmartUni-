package com.smartuni.booking;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VenueBookingController {

    private final VenueBookingService venueBookingService;

    @GetMapping("/bookings")
    public List<BookingResponse> listBookings() {
        return venueBookingService.listBookings();
    }

    @GetMapping("/venues/{venueId}/bookings")
    public List<BookingResponse> listBookingsForVenue(@PathVariable Long venueId) {
        return venueBookingService.listBookingsForVenue(venueId);
    }

    // TODO-BE-014: validation is not triggered for this body yet.
    @PostMapping("/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@RequestBody CreateBookingRequest request) {
        return venueBookingService.createBooking(request);
    }
}
