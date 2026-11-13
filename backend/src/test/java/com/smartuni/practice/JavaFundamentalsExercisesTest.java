package com.smartuni.practice;

import static com.smartuni.practice.JavaFundamentalsExercises.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Run individual tasks from backend/: ./run-task.sh JAVA-001
 * Tests are tagged "todo" and excluded from baseline mvn test until you
 * explicitly select a task. Do not change expected values to make tests pass.
 */
@Tag("todo")
class JavaFundamentalsExercisesTest {

    @Test
    @Tag("JAVA-001")
    void normalizesDistinctCourseCodes() {
        assertThat(normalizeCourseCodes(
                List.of(" cs101 ", "CS101", " ", "math110", " cs201 ", "CS201")))
                .containsExactly("CS101", "CS201", "MATH110");
        assertThat(normalizeCourseCodes(List.of(" ", ""))).isEmpty();
    }

    @Test
    @Tag("JAVA-002")
    void countsOnlyActiveEnrollmentsPerCourse() {
        List<EnrollmentRow> rows = List.of(
                new EnrollmentRow(1L, "CS101", "ACTIVE"),
                new EnrollmentRow(2L, "CS101", "ACTIVE"),
                new EnrollmentRow(3L, "CS101", "CANCELLED"),
                new EnrollmentRow(4L, "CS201", "ACTIVE"),
                new EnrollmentRow(5L, "MATH110", "CANCELLED"));
        assertThat(activeEnrollmentCounts(rows))
                .containsExactlyInAnyOrderEntriesOf(Map.of("CS101", 2L, "CS201", 1L));
        assertThat(activeEnrollmentCounts(List.of())).isEmpty();
    }

    @Test
    @Tag("JAVA-003")
    void formatsValidationIssuesForApiResponse() {
        List<ValidationIssue> problems = List.of(
                new ValidationIssue("email", "must be valid"),
                new ValidationIssue("title", "must not be blank"));
        assertThat(formatValidationIssues(problems))
                .isEqualTo("email: must be valid, title: must not be blank");
        assertThat(formatValidationIssues(List.of())).isEmpty();
    }

    @Test
    @Tag("JAVA-004")
    void indexesStudentsAndKeepsLastDuplicate() {
        StudentRow original = new StudentRow(10L, "Tendai", "Moyo");
        StudentRow updated = new StudentRow(10L, "Tendai", "Nyathi");
        StudentRow other = new StudentRow(20L, "Farai", "Dube");
        assertThat(indexStudentsById(List.of(original, other, updated)))
                .containsExactlyInAnyOrderEntriesOf(Map.of(10L, updated, 20L, other));
    }

    @Test
    @Tag("JAVA-005")
    void sortsBySurnameThenGivenNameWithoutMutatingOriginal() {
        StudentRow c = new StudentRow(3L, "Tendai", "Moyo");
        StudentRow b = new StudentRow(2L, "Rutendo", "Dube");
        StudentRow a = new StudentRow(1L, "Farai", "Dube");
        List<StudentRow> input = new ArrayList<>(List.of(c, b, a));
        assertThat(sortStudentsByName(input)).containsExactly(a, b, c);
        assertThat(input).containsExactly(c, b, a);
    }

    @Test
    @Tag("JAVA-006")
    void findsStudentOrThrowsMeaningfulException() {
        StudentRow student = new StudentRow(4L, "Nyasha", "Moyo");
        assertThat(requireStudent(List.of(student), 4L)).isEqualTo(student);
        assertThatThrownBy(() -> requireStudent(List.of(student), 999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Student not found: 999");
    }

    @Test
    @Tag("JAVA-007")
    void flattensModulesWithoutDroppingDuplicates() {
        List<FacultyCourses> faculties = List.of(
                new FacultyCourses("Engineering", List.of("CS101", "CS201")),
                new FacultyCourses("Science", List.of("CS101", "BIO110")));
        assertThat(flattenCourseCodes(faculties))
                .containsExactly("CS101", "CS201", "CS101", "BIO110");
        assertThat(flattenCourseCodes(List.of())).isEmpty();
    }

    @Test
    @Tag("JAVA-008")
    void acceptsCallerSuppliedLambdaPredicate() {
        StudentRow a = new StudentRow(1L, "Tendai", "Moyo");
        StudentRow b = new StudentRow(2L, "Farai", "Dube");
        StudentRow c = new StudentRow(3L, "Tatenda", "Dube");
        assertThat(filterStudents(List.of(a, b, c), student -> student.lastName().equals("Dube")))
                .containsExactly(b, c);
        assertThat(filterStudents(List.of(a, b), student -> student.id() > 100)).isEmpty();
    }

    @Test
    @Tag("JAVA-009")
    void preservesOrderAndWorksForMultipleGenericTypes() {
        assertThat(uniqueInOrder(List.of("CS101", "CS201", "CS101", "SE350")))
                .containsExactly("CS101", "CS201", "SE350");
        assertThat(uniqueInOrder(List.of(3, 1, 3, 2, 1)))
                .containsExactly(3, 1, 2);
        assertThat(uniqueInOrder(List.of())).isEmpty();
    }
}
