package com.smartuni.venue;

public record VenueResponse(Long id, String name, String building, String roomNumber, int capacity) {

    public static VenueResponse from(Venue venue) {
        return new VenueResponse(venue.getId(), venue.getName(), venue.getBuilding(), venue.getRoomNumber(), venue.getCapacity());
    }
}
