package com.smartuni.student;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP layer only: bind the request, call the service, shape the response. No business rules here.
 */
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<StudentResponse> listStudents() {
        return studentService.listStudents();
    }

    @GetMapping("/{id}")
    public StudentResponse getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    // TODO-BE-001: POST /api/students
    //   Bind the JSON body, delegate to StudentService.registerStudent, and respond with
    //   201 Created plus a Location header (/api/students/{id}). Which return type lets you set status + headers?
    //   Also needed for TODO-BE-002: trigger validation on the body.

    // TODO-BE-004: GET /api/students/search?name=...
    //   Bind the query parameter and delegate to StudentService.searchStudents.
    //   Careful: "/search" and "/{id}" both match GET /api/students/search. How does Spring choose?

    // TODO-BE-007: PATCH /api/students/{id}/status?active=true|false
    //   Combine @PathVariable and @RequestParam. Return the updated student.
}
