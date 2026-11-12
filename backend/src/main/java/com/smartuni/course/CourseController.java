package com.smartuni.course;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public List<CourseResponse> listCourses() {
        return courseService.listCourses();
    }

    @GetMapping("/{id}")
    public CourseResponse getCourse(@RequestParam Long id) {
        return courseService.getCourse(id);
    }

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        CourseResponse createdCourse = courseService.createCourse(request);
        return ResponseEntity.created(URI.create("/api/courses/" + createdCourse.id())).body(createdCourse);
    }
}
