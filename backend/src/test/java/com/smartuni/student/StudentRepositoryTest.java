package com.smartuni.student;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Example of a JPA SLICE test. @DataJpaTest starts only JPA: entities, repositories, an embedded database.
 * No controllers, no services. Each test runs in a transaction that is rolled back.
 *
 * TODO-BE-022 (P2): add a test here for the search method you wrote in TODO-BE-004.
 * Save your own students in the test instead of relying on data.sql so the test documents its own data.
 */
@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void findsStudentByEmail() {
        Student student = Student.builder()
                .firstName("Grace")
                .lastName("Hopper")
                .email("grace.hopper@test.smartuni.ac.zw")
                .build();
        studentRepository.save(student);

        assertThat(studentRepository.findByEmail("grace.hopper@test.smartuni.ac.zw"))
                .get()
                .extracting(Student::getLastName)
                .isEqualTo("Hopper");
    }
}
