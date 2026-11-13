package com.smartuni.booking;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VenueBookingRepository extends JpaRepository<VenueBooking, Long> {

    Page<VenueBooking> findAllByVenueId(Long venueId, Pageable pageable);

    // TODO-BE-015: does the venue already have a booking overlapping [start, end)?
    //   Draw two intervals on paper first. Two half-open ranges overlap when ...?
    //   A derived "exists" method is possible but long; a JPQL query may read better. Your call.

    @Query("""
            select count(booking) > 0
            from VenueBooking booking
            where booking.venue.id = :venueId
              and booking.startTime < :endTime
              and booking.endTime > :startTime
            """)
    boolean existsOverlappingBooking(
            @Param("venueId") Long venueId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    // TODO-BE-019: listing all bookings triggers N+1 lazy loads (venue + course per row).
    //   Add a query that loads bookings together with venue and course in a single SELECT.
}
