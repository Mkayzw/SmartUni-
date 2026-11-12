package com.smartuni.enrollment;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentResponse enroll(@Valid @RequestBody CreateEnrollmentRequest request) {
        return enrollmentService.enroll(request);
    }

    @PostMapping("/enrollments/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public List<EnrollmentResponse> enrollInCourses(@Valid @RequestBody BatchEnrollmentRequest request) {
        return enrollmentService.enrollInCourses(request);
    }

    // TODO-BE-010: PATCH /api/enrollments/{enrollmentId}/cancel  -> 200 with the updated EnrollmentResponse
    //   Why PATCH and not DELETE? Be ready to argue both sides.

    // TODO-BE-011: GET /api/students/{studentId}/courses  -> 200 with List<CourseResponse>
}
