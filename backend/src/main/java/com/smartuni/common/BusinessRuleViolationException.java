package com.smartuni.common;

/**
 * Thrown when a request is well-formed but breaks a business rule
 * (duplicate enrollment, course full, overlapping booking, duplicate email...).
 *
 * It is unchecked on purpose: Spring's @Transactional rolls back on RuntimeException by default.
 *
 * Which HTTP status should this become? That decision is part of TODO-BE-003.
 */
public class BusinessRuleViolationException extends RuntimeException {

    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
