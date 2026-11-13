package com.smartuni.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@Tag("todo")
@Tag("BE-021")
class BookingNotifierProfileTest {

    @Nested
    @SpringBootTest
    @ActiveProfiles("dev")
    class WithDevProfile {

        @Autowired
        private ApplicationContext applicationContext;

        @Test
        void usesConsoleNotifier() {
            BookingNotifier notifier = applicationContext.getBean(BookingNotifier.class);
            assertThat(notifier.getClass().getSimpleName()).isEqualTo("ConsoleBookingNotifier");
        }
    }

    @Nested
    @SpringBootTest
    class WithoutDevProfile {

        @Autowired
        private ApplicationContext applicationContext;

        @Test
        void usesNoOpNotifier() {
            BookingNotifier notifier = applicationContext.getBean(BookingNotifier.class);
            assertThat(notifier.getClass().getSimpleName()).isEqualTo("NoOpBookingNotifier");
        }
    }
}
