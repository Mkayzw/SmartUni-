package com.smartuni.enrollment;

import com.smartuni.course.CourseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * The heart of the practice project. Most business rules live here.
 *
 * Decide yourself which methods need @Transactional and whether readOnly applies.
 * Decide which repositories to inject (constructor injection only).
 */
@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    /**
     * TODO-BE-009: Enroll a student in a course.
     * Rules (in an order that gives sensible error messages):
     *   - student must exist           -> ResourceNotFoundException (404)
     *   - course must exist            -> ResourceNotFoundException (404)
     *   - student must be active       -> BusinessRuleViolationException (409)
     *   - no ACTIVE duplicate          -> BusinessRuleViolationException (409)
     *   - course must not be full      -> BusinessRuleViolationException (409)
     * New enrollments are ACTIVE with enrolledAt = now. Build them with Enrollment.builder() (project convention).
     *
     * Test: EnrollmentWorkflowTest
     */
    public EnrollmentResponse enroll(CreateEnrollmentRequest request) {
        throw new UnsupportedOperationException("TODO-BE-009: implement enrollment");
    }

    /**
     * TODO-BE-010: Cancel an enrollment (soft delete: status becomes CANCELLED, row stays).
     *   - unknown id -> 404
     *   - already cancelled -> 409
     * A cancelled enrollment must free up a place in the course, and the student may enroll again later.
     */
    public EnrollmentResponse cancel(Long enrollmentId) {
        throw new UnsupportedOperationException("TODO-BE-010: implement cancellation");
    }

    /**
     * TODO-BE-011: The courses a student is ACTIVELY enrolled in. Unknown student -> 404.
     */
    public List<CourseResponse> getCoursesForStudent(Long studentId) {
        throw new UnsupportedOperationException("TODO-BE-011: implement courses for student");
    }

    /**
     * Lists every enrollment, newest first, for the Enrollments screen.
     * Part of TODO-BE-009 (you need the relationships to build EnrollmentResponse).
     */
    public List<EnrollmentResponse> listEnrollments() {
        throw new UnsupportedOperationException("TODO-BE-009: implement listing enrollments");
    }

    /**
     * TODO-BE-018: Enroll a student in several courses ALL-OR-NOTHING.
     * If any single enrollment fails (e.g. the third course is full), none of them may be saved.
     * Reuse your single-enrollment rules. Then answer: if you call this.enroll(...) from here,
     * does enroll()'s own @Transactional annotation apply? Why or why not?
     *
     * Test: BatchEnrollmentRollbackTest
     */
    public List<EnrollmentResponse> enrollInCourses(BatchEnrollmentRequest request) {
        throw new UnsupportedOperationException("TODO-BE-018: implement batch enrollment");
    }
}
