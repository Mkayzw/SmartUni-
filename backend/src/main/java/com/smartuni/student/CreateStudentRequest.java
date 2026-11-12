package com.smartuni.student;

/**
 * Request body for POST /api/students.
 *
 * TODO-BE-002: add bean validation annotations to the components of this record.
 *   - firstName and lastName: must not be blank
 *   - email: must be present AND a well-formed email address
 * Then make sure the controller actually triggers validation (annotations alone do nothing).
 * Which annotation fails on null but allows ""? Which one rejects both? Be ready to explain.
 *
 * Test: StudentControllerTest
 */
public record CreateStudentRequest(String firstName, String lastName, String email) {
}
