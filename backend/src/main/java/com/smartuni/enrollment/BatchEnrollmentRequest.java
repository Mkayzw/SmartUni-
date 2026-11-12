package com.smartuni.enrollment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record BatchEnrollmentRequest(@NotNull Long studentId, @NotEmpty List<Long> courseIds) {
}
