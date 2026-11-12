package com.smartuni;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point.
 *
 * @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan.
 * Component scanning starts at THIS package (com.smartuni) and walks down, which is why
 * every feature package lives underneath it. A @Service in com.other would never be found.
 */
@SpringBootApplication
public class SmartUniApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartUniApplication.class, args);
    }
}
