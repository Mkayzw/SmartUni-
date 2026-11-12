package com.smartuni.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    long countByStatus(EnrollmentStatus status);

    // TODO-BE-006: count ACTIVE enrollments for a course (derived query).
    // TODO-BE-009: check whether a student already has an ACTIVE enrollment in a course (derived query).
    // TODO-BE-011: find the courses a student is actively enrolled in.
    //   A derived query on this repository returns Enrollments; you want Courses.
    //   Decide between: mapping enrollments -> courses in the service, or a JPQL query that selects e.course.
    //   Whichever you pick, avoid N+1 lazy loading (see INTERVIEW_THEORY.md).
}
