package com.smartuni.venue;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {

    List<Venue> findAllByOrderByBuildingAscRoomNumberAsc();

    boolean existsByBuildingAndRoomNumber(String building, String roomNumber);

    // TODO-BE-020: find venues with capacity >= minCapacity that have NO booking overlapping [start, end).
    //   A derived query cannot express "not exists"; write JPQL with @Query and named parameters.
}
