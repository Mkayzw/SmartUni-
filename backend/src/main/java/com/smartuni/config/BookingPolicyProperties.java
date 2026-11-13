package com.smartuni.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalTime;

/**
 * TODO-BE-013: Bind the "smartuni.booking" block of application.yml to this record.
 *
 * Today this is a plain record: Spring does not know about it, so it is not a bean and nothing is bound.
 * Your job:
 *   1. Make Spring bind it with @ConfigurationProperties(prefix = "smartuni.booking").
 *   2. Make Spring REGISTER it as a bean. Annotating the record is not enough on its own.
 *      There are two common ways (@EnableConfigurationProperties on a @Configuration class,
 *      or @ConfigurationPropertiesScan on the application class). Choose one and be able to defend it.
 *   3. Use it in VenueBookingService (TODO-BE-014) instead of hard-coded numbers.
 *
 * Why not @Value? Compare in INTERVIEW_THEORY.md: typed, grouped, validated, relaxed binding.
 *
 * Test: BookingPolicyPropertiesTest
 */
@ConfigurationProperties(prefix="smartuni.booking")
public record BookingPolicyProperties(
        int minDurationMinutes,
        int maxDurationMinutes,
        LocalTime openingTime,
        LocalTime closingTime
) {
}
