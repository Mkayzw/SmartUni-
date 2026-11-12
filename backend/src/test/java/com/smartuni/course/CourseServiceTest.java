package com.smartuni.course;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.smartuni.common.BusinessRuleViolationException;
import com.smartuni.enrollment.EnrollmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Example of a pure UNIT test: no Spring at all. Mockito creates fake repositories,
 * @InjectMocks calls the service constructor with them. Runs in milliseconds.
 *
 * EnrollmentRepository is mocked already so this test keeps compiling and passing after you inject it in TODO-BE-006.
 */
@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void createsCourseWithRequestedValues() {
        when(courseRepository.existsByCode("CS999")).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
            Course course = invocation.getArgument(0);
            course.setId(77L);
            return course;
        });

        CourseResponse response = courseService.createCourse(new CreateCourseRequest("CS999", "Compilers", 20));

        ArgumentCaptor<Course> savedCourse = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(savedCourse.capture());
        assertThat(savedCourse.getValue().getCapacity()).isEqualTo(20);
        assertThat(response.id()).isEqualTo(77L);
        assertThat(response.availablePlaces()).isEqualTo(20);
    }

    @Test
    void rejectsDuplicateCourseCode() {
        when(courseRepository.existsByCode("CS101")).thenReturn(true);

        assertThatThrownBy(() -> courseService.createCourse(new CreateCourseRequest("CS101", "Again", 10)))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("CS101");

        verify(courseRepository, never()).save(any());
    }
}
