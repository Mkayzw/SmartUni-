package com.smartuni.venue;

import jakarta.validation.constraints.Min;

public record UpdateVenueCapacityRequest(@Min(1) int capacity) {
}
