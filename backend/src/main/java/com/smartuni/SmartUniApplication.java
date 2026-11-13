package com.smartuni;

import com.smartuni.config.BookingPolicyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Entry point.
 *
 * @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan.
 * Component scanning starts at THIS package (com.smartuni) and walks down, which is why
 * every feature package lives underneath it. A @Service in com.other would never be found.
 */
@SpringBootApplication
@EnableConfigurationProperties(BookingPolicyProperties.class)

public class SmartUniApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartUniApplication.class, args);
    }
}
