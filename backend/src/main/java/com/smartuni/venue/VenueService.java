package com.smartuni.venue;

import com.smartuni.common.BusinessRuleViolationException;
import com.smartuni.common.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;

    @Transactional(readOnly = true)
    public List<VenueResponse> listVenues() {
        return venueRepository.findAllByOrderByBuildingAscRoomNumberAsc().stream()
                .map(VenueResponse::from)
                .toList();
    }

    @Transactional
    public VenueResponse createVenue(CreateVenueRequest request) {
        if (venueRepository.existsByBuildingAndRoomNumber(request.building(), request.roomNumber())) {
            throw new BusinessRuleViolationException(
                    "Room " + request.roomNumber() + " in " + request.building() + " is already registered");
        }

        Venue venue = Venue.builder()
                .name(request.name())
                .building(request.building())
                .roomNumber(request.roomNumber())
                .capacity(request.capacity())
                .build();

        return VenueResponse.from(venueRepository.save(venue));
    }

    public VenueResponse updateCapacity(Long id, int newCapacity) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue", id));
        venue.setCapacity(newCapacity);
        return VenueResponse.from(venue);
    }

    /**
     * TODO-BE-020: venues free for the whole of [start, end) with at least minCapacity seats.
     * Reject end <= start with a BusinessRuleViolationException.
     */
    public List<VenueResponse> findAvailableVenues(LocalDateTime start, LocalDateTime end, int minCapacity) {
        throw new UnsupportedOperationException("TODO-BE-020: implement available venue search");
    }
}
