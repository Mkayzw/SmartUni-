package com.smartuni.booking;

import com.smartuni.course.Course;
import com.smartuni.venue.Venue;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A finished example of @ManyToOne mappings. Compare it with Enrollment (TODO-BE-008).
 *
 * Time ranges are half-open: [startTime, endTime). A booking ending at 10:00 does not overlap
 * one starting at 10:00.
 */
@Entity
@Table(name = "venue_bookings")
@Getter
@Setter
@NoArgsConstructor
public class VenueBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private int expectedAttendance;
}
