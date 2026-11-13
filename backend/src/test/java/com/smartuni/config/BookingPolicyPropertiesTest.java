package com.smartuni.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest(properties = "smartuni.booking.max-duration-minutes=180")
@Tag("todo")
@Tag("BE-013")
class BookingPolicyPropertiesTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void bindsTheSmartuniBookingBlock() {
        BookingPolicyProperties properties = applicationContext.getBean(BookingPolicyProperties.class);

        assertThat(properties.minDurationMinutes()).isEqualTo(30);
        assertThat(properties.maxDurationMinutes()).as("overridden by the test property").isEqualTo(180);
        assertThat(properties.openingTime()).isEqualTo(LocalTime.of(7, 0));
        assertThat(properties.closingTime()).isEqualTo(LocalTime.of(21, 0));
    }
}
