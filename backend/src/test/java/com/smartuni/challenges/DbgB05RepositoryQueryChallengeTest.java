package com.smartuni.challenges;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartuni.venue.Venue;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * DBG-B05: Booking staff say "the room finder never suggests Computer Lab 1 for a 35-person class,
 * and it suggests the huge theatre first". Venues in data.sql: 200, 35, 20, 120 seats.
 */
@DataJpaTest
@Tag("challenge")
@Tag("DBG-B05")
class DbgB05RepositoryQueryChallengeTest {

    @Autowired
    private ChallengeVenueRepository challengeVenueRepository;

    @Test
    void findsVenuesThatCanSeatExactlyTheAttendeesSmallestFirst() {
        List<Venue> venues = challengeVenueRepository.findVenuesThatFit(35);

        assertThat(venues).extracting(Venue::getCapacity).containsExactly(35, 120, 200);
    }
}
