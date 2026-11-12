package com.smartuni.venue;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    @GetMapping
    public List<VenueResponse> listVenues() {
        return venueService.listVenues();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VenueResponse createVenue(@RequestBody CreateVenueRequest request) {
        return venueService.createVenue(request);
    }

    @PatchMapping("/{id}/capacity")
    public VenueResponse updateCapacity(@PathVariable Long id, @Valid @RequestBody UpdateVenueCapacityRequest request) {
        return venueService.updateCapacity(id, request.capacity());
    }

    /** Example: GET /api/venues/available?start=2030-01-10T09:00&end=2030-01-10T11:00&minCapacity=40 */
    @GetMapping("/available")
    public List<VenueResponse> findAvailableVenues(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "1") int minCapacity) {
        return venueService.findAvailableVenues(start, end, minCapacity);
    }
}
