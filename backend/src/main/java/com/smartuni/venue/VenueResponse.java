package com.smartuni.venue;

public record VenueResponse(
        Long id,
        String name,
        String building,
        String roomNumber,
        int capacity
) {
}
