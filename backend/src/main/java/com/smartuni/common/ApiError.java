package com.smartuni.common;

import java.time.Instant;
import java.util.Map;

/**
 * The single JSON error shape the frontend can rely on.
 *
 * fieldErrors is only populated for validation failures, e.g. {"email": "must be a well-formed email address"}.
 */
public record ApiError(
        int status,
        String error,
        String message,
        String path,
        Instant timestamp,
        Map<String, String> fieldErrors
) {

    public static ApiError of(int status, String error, String message, String path) {
        return new ApiError(status, error, message, path, Instant.now(), Map.of());
    }
}
