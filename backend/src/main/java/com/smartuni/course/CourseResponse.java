package com.smartuni.course;

public record CourseResponse(Long id, String code, String title, int capacity, int availablePlaces) {
}
