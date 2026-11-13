package com.smartuni.challenges;

import com.smartuni.venue.Venue;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Used by DBG-B05. */
public interface ChallengeVenueRepository extends JpaRepository<Venue, Long> {

    /** Venues that can seat the given number of attendees, smallest first. */
    @Query("select v from Venue v where v.capacity > :attendees order by v.capacity desc")
    List<Venue> findVenuesThatFit(@Param("attendees") int attendees);
}
