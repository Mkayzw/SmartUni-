package com.smartuni.enrollment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A student's place in a course.
 *
 * TODO-BE-008: Replace the raw foreign-key columns with real JPA relationships.
 *
 *   Today:   private Long studentId;   private Long courseId;
 *   Target:  a Student field and a Course field, both many-to-one, lazily fetched,
 *            joined on the SAME column names (student_id, course_id) so the seed data in data.sql keeps working.
 *
 *   Questions to answer for yourself:
 *     - Which side owns the relationship (has the foreign key)? Why is it always the "many" side?
 *     - What is the default fetch type of @ManyToOne, and why do most teams override it?
 *     - After the change, does EnrollmentRepository.countByCourseIdAndStatus(...) still work? Why?
 *     - Do you need the inverse @OneToMany on Student/Course? (See TODO-BE-008 notes in TASKS.md.)
 *
 * Test: EnrollmentMappingTest
 */
@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnrollmentStatus status;

    @Column(nullable = false)
    private LocalDateTime enrolledAt;
}
