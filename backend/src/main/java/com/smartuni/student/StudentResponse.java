package com.smartuni.student;

/** What the API returns for a student. Deliberately not the entity itself. */
public record StudentResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        boolean active
) {
}
