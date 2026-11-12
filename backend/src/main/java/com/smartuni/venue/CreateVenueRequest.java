package com.smartuni.venue;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateVenueRequest(
        @NotBlank String name,
        @NotBlank String building,
        @NotBlank String roomNumber,
        @Min(1) int capacity
) {
}
