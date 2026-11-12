package com.smartuni.course;

import com.smartuni.common.BusinessRuleViolationException;
import com.smartuni.common.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<CourseResponse> listCourses() {
        return courseRepository.findAllByOrderByCodeAsc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
        return toResponse(course);
    }

    @Transactional
    public CourseResponse createCourse(CreateCourseRequest request) {
        if (courseRepository.existsByCode(request.code())) {
            throw new BusinessRuleViolationException("A course with code " + request.code() + " already exists");
        }

        Course course = new Course();
        course.setCode(request.code());
        course.setTitle(request.title());
        course.setCapacity(request.capacity());

        Course savedCourse = courseRepository.save(course);
        return toResponse(savedCourse);
    }

    /**
     * TODO-BE-006: availablePlaces is currently a lie: it always equals capacity.
     *   availablePlaces = capacity - number of ACTIVE enrollments for this course.
     *   Add the counting query to EnrollmentRepository (derived query) and inject that repository here.
     *   Cancelled enrollments must not count.
     *
     * Follow-up question for the interview: listCourses() calls this once per course.
     * How many SQL queries does GET /api/courses run for 50 courses? Is that acceptable here? (P2: BE-019 is similar.)
     */
    private CourseResponse toResponse(Course course) {
        int availablePlaces = course.getCapacity();
        return new CourseResponse(course.getId(), course.getCode(), course.getTitle(), course.getCapacity(), availablePlaces);
    }
}
