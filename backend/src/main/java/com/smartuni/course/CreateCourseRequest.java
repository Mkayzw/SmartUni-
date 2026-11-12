package com.smartuni.course;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * A finished example of a validated request DTO. Compare it with CreateStudentRequest (TODO-BE-002).
 */
public record CreateCourseRequest(
        @NotBlank
        @Pattern(regexp = "[A-Z]{2,4}[0-9]{3}", message = "must look like CS101")
        String code,

        @NotBlank
        String title,

        @Min(1)
        @Max(500)
        int capacity
) {
}
