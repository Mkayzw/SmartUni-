package com.smartuni.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
@Tag("todo")
@Tag("BE-012")
class ClockConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void exactlyOneClockBeanIsDefined() {
        assertThat(applicationContext.getBeansOfType(Clock.class)).hasSize(1);
    }
}
