package com.smartuni.challenges;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.smartuni.student.Student;
import com.smartuni.student.StudentRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

/**
 * DBG-B03: When the welcome email fails, the student row must NOT remain in the database.
 * The method is annotated @Transactional, yet the student is still saved. Why?
 */
@SpringBootTest
@Tag("challenge")
@Tag("DBG-B03")
class DbgB03TransactionChallengeTest {

    static class WelcomeEmailException extends Exception {
        WelcomeEmailException(String message) {
            super(message);
        }
    }

    static class OnboardingService {

        private final StudentRepository studentRepository;

        OnboardingService(StudentRepository studentRepository) {
            this.studentRepository = studentRepository;
        }

        @Transactional
        public void onboard(String email) throws WelcomeEmailException {
            Student student = Student.builder()
                    .firstName("Temp")
                    .lastName("Student")
                    .email(email)
                    .build();
            studentRepository.save(student);

            sendWelcomeEmail(email);
        }

        private void sendWelcomeEmail(String email) throws WelcomeEmailException {
            throw new WelcomeEmailException("Mail server unavailable for " + email);
        }
    }

    @TestConfiguration
    static class OnboardingConfig {

        @Bean
        OnboardingService onboardingService(StudentRepository studentRepository) {
            return new OnboardingService(studentRepository);
        }
    }

    @Autowired
    private OnboardingService onboardingService;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void failedOnboardingLeavesNoStudentBehind() {
        String email = "dbg-b03-" + System.nanoTime() + "@test.smartuni.ac.zw";

        assertThatThrownBy(() -> onboardingService.onboard(email))
                .isInstanceOf(WelcomeEmailException.class);

        assertThat(studentRepository.findByEmail(email)).isEmpty();
    }
}
