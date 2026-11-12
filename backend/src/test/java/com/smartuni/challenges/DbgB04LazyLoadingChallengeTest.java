package com.smartuni.challenges;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartuni.booking.VenueBooking;
import com.smartuni.booking.VenueBookingRepository;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * DBG-B04: A report of which venues are booked explodes with an exception. It worked on a colleague's
 * machine "where open-in-view was on". Make it work with open-in-view=false (our setting).
 */
@SpringBootTest
@Tag("challenge")
@Tag("DBG-B04")
class DbgB04LazyLoadingChallengeTest {

    static class BookingReport {

        private final VenueBookingRepository venueBookingRepository;

        BookingReport(VenueBookingRepository venueBookingRepository) {
            this.venueBookingRepository = venueBookingRepository;
        }

        public List<String> bookedVenueNames() {
            List<VenueBooking> bookings = venueBookingRepository.findAll();
            return bookings.stream()
                    .map(booking -> booking.getVenue().getName())
                    .distinct()
                    .toList();
        }
    }

    @TestConfiguration
    static class ReportConfig {

        @Bean
        BookingReport bookingReport(VenueBookingRepository venueBookingRepository) {
            return new BookingReport(venueBookingRepository);
        }
    }

    @Autowired
    private BookingReport bookingReport;

    @Test
    void listsBookedVenueNames() {
        assertThat(bookingReport.bookedVenueNames()).contains("Main Lecture Theatre", "Seminar Room");
    }
}
