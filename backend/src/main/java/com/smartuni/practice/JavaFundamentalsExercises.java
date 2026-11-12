package com.smartuni.practice;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Interview practice: implement these methods yourself.
 *
 * Each TODO has a tagged test. Run ./run-task.sh JAVA-001 from backend/, and
 * change only the method for that task until the test passes.
 *
 * These exercises are deliberately independent of Spring, JPA and the
 * SmartUni entities. You can practise Java without first finishing BE-008.
 * Keep the existing method signatures and do not remove the tests.
 */
public final class JavaFundamentalsExercises {

    private JavaFundamentalsExercises() {
    }

    public record StudentRow(long id, String firstName, String lastName) {
    }

    public record EnrollmentRow(long studentId, String courseCode, String status) {
    }

    public record ValidationIssue(String field, String message) {
    }

    public record FacultyCourses(String faculty, List<String> courseCodes) {
    }

    /**
     * TODO-JAVA-001: Stream pipeline with filter, map, distinct and sorted.
     * Trim codes, discard blanks, uppercase them, remove duplicates and sort.
     * Example: [" cs101 ", "CS101", " ", "math110"] -> ["CS101", "MATH110"].
     * The input list is never null and contains no null elements.
     */
    public static List<String> normalizeCourseCodes(List<String> codes) {
        throw new UnsupportedOperationException("TODO-JAVA-001");
    }

    /**
     * TODO-JAVA-002: Stream filter + Collectors.groupingBy/counting.
     * Count ACTIVE enrollments per course code. CANCELLED rows do not count.
     * Courses without an ACTIVE enrollment need not appear in the map.
     */
    public static Map<String, Long> activeEnrollmentCounts(List<EnrollmentRow> enrollments) {
        throw new UnsupportedOperationException("TODO-JAVA-002");
    }

    /**
     * TODO-JAVA-003: map + Collectors.joining.
     * Convert issues to "field: message" and join them with ", ".
     * Empty list -> empty string. Preserve issue order.
     * Similar to formatting Bean Validation errors in RestExceptionHandler.
     */
    public static String formatValidationIssues(List<ValidationIssue> issues) {
        throw new UnsupportedOperationException("TODO-JAVA-003");
    }

    /**
     * TODO-JAVA-004: Collectors.toMap and a merge function.
     * Index students by id. If an id occurs twice, the LAST row wins.
     * Empty input -> empty map.
     */
    public static Map<Long, StudentRow> indexStudentsById(List<StudentRow> students) {
        throw new UnsupportedOperationException("TODO-JAVA-004");
    }

    /**
     * TODO-JAVA-005: Comparator.comparing + thenComparing.
     * Return a NEW list ordered by lastName, then firstName, ascending.
     * Names are non-null; case-sensitive lexical order is acceptable.
     * Do not mutate the input list.
     */
    public static List<StudentRow> sortStudentsByName(List<StudentRow> students) {
        throw new UnsupportedOperationException("TODO-JAVA-005");
    }

    /**
     * TODO-JAVA-006: Optional / findFirst / orElseThrow.
     * Find a student by id, or throw java.util.NoSuchElementException
     * with exact message "Student not found: <id>".
     */
    public static StudentRow requireStudent(List<StudentRow> students, long id) {
        throw new UnsupportedOperationException("TODO-JAVA-006");
    }

    /**
     * TODO-JAVA-007: flatMap.
     * Flatten the nested list of codes across faculties into one list.
     * Preserve input order; keep duplicates.
     */
    public static List<String> flattenCourseCodes(List<FacultyCourses> faculties) {
        throw new UnsupportedOperationException("TODO-JAVA-007");
    }

    /**
     * TODO-JAVA-008: Predicate<T> and lambdas.
     * Return students for which predicate.test(student) is true.
     * Preserve input order. Do not mutate the source.
     */
    public static List<StudentRow> filterStudents(
            List<StudentRow> students, Predicate<StudentRow> predicate) {
        throw new UnsupportedOperationException("TODO-JAVA-008");
    }

    /**
     * TODO-JAVA-009: Generic methods and Set vs List.
     * Keep only the first occurrence of each value, preserving input order.
     * Must work with List<String>, List<Integer> and any other object type.
     * Return a NEW list.
     */
    public static <T> List<T> uniqueInOrder(List<T> values) {
        throw new UnsupportedOperationException("TODO-JAVA-009");
    }
}
