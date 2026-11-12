package com.smartuni.enrollment;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Note that this controller is mapped to /api, not /api/enrollments, because one endpoint
 * (courses for a student) lives under /api/students/... . Keeping it here keeps the enrollment
 * feature in one package.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/enrollments")
    public List<EnrollmentResponse> listEnrollments() {
        return enrollmentService.listEnrollments();
    }

    @PostMapping("/enrollments")
    public ResponseEntity<EnrollmentResponse> enroll(@Valid @RequestBody CreateEnrollmentRequest request) {
        EnrollmentResponse createdEnrollment = enrollmentService.enroll(request);
        return ResponseEntity.created(URI.create("/api/enrollments/" + createdEnrollment.id())).body(createdEnrollment);
    }

    /** Several resources are created, so there is no single Location to point at: plain 201 with the list. */
    @PostMapping("/enrollments/batch")
    public ResponseEntity<List<EnrollmentResponse>> enrollInCourses(@Valid @RequestBody BatchEnrollmentRequest request) {
        List<EnrollmentResponse> createdEnrollments = enrollmentService.enrollInCourses(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEnrollments);
    }

    // TODO-BE-010: PATCH /api/enrollments/{enrollmentId}/cancel  -> 200 with the updated EnrollmentResponse
    //   Why PATCH and not DELETE? Be ready to argue both sides.

    // TODO-BE-011: GET /api/students/{studentId}/courses  -> 200 with List<CourseResponse>
}
