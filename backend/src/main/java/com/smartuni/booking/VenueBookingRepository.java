package com.smartuni.booking;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueBookingRepository extends JpaRepository<VenueBooking, Long> {

    List<VenueBooking> findAllByOrderByStartTimeAsc();

    List<VenueBooking> findAllByVenueIdOrderByStartTimeAsc(Long venueId);

    // TODO-BE-015: does the venue already have a booking overlapping [start, end)?
    //   Draw two intervals on paper first. Two half-open ranges overlap when ...?
    //   A derived "exists" method is possible but long; a JPQL query may read better. Your call.

    // TODO-BE-019: listing all bookings triggers N+1 lazy loads (venue + course per row).
    //   Add a query that loads bookings together with venue and course in a single SELECT.
}
