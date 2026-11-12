package com.smartuni.enrollment;

import java.time.LocalDateTime;

/**
 * The API shape for an enrollment. The frontend shows student and course names, so the response
 * carries them. Building this from an Enrollment requires the relationships from TODO-BE-008.
 */
public record EnrollmentResponse(
        Long id,
        Long studentId,
        String studentName,
        Long courseId,
        String courseCode,
        String courseTitle,
        EnrollmentStatus status,
        LocalDateTime enrolledAt
) {
}
