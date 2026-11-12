package com.smartuni.config;

/**
 * Something that tells the outside world a booking was made.
 *
 * TODO-BE-021 (profiles): provide two implementations, each a Spring bean:
 *   - ConsoleBookingNotifier: active ONLY when the "dev" profile is active. Logs the message.
 *   - NoOpBookingNotifier: active whenever "dev" is NOT active. Does nothing.
 * Exactly one bean of this type must exist in every profile. Put the implementations in this package.
 *
 * Test: BookingNotifierProfileTest
 */
public interface BookingNotifier {

    void bookingCreated(String description);
}
