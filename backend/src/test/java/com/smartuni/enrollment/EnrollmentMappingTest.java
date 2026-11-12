package com.smartuni.enrollment;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartuni.course.Course;
import com.smartuni.student.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Inspects the JPA metamodel (what Hibernate understood from your annotations) instead of your Java source.
 */
@DataJpaTest
@Tag("todo")
@Tag("BE-008")
class EnrollmentMappingTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    void enrollmentHasManyToOneToStudent() {
        EntityType<Enrollment> enrollmentType = entityManager.getMetamodel().entity(Enrollment.class);
        Attribute<? super Enrollment, ?> student = enrollmentType.getAttribute("student");

        assertThat(student.getPersistentAttributeType()).isEqualTo(Attribute.PersistentAttributeType.MANY_TO_ONE);
        assertThat(student.getJavaType()).isEqualTo(Student.class);
    }

    @Test
    void enrollmentHasManyToOneToCourse() {
        EntityType<Enrollment> enrollmentType = entityManager.getMetamodel().entity(Enrollment.class);
        Attribute<? super Enrollment, ?> course = enrollmentType.getAttribute("course");

        assertThat(course.getPersistentAttributeType()).isEqualTo(Attribute.PersistentAttributeType.MANY_TO_ONE);
        assertThat(course.getJavaType()).isEqualTo(Course.class);
    }

    @Test
    void seedDataStillLoadsThroughTheNewColumns() {
        // If you renamed the join columns, data.sql would have failed and this count would be 0.
        assertThat(enrollmentRepository.count()).isEqualTo(7);
    }
}
