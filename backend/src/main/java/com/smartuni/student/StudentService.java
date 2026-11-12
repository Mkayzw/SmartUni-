package com.smartuni.student;

import com.smartuni.common.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public List<StudentResponse> listStudents() {
        return studentRepository.findAllByOrderByLastNameAscFirstNameAsc().stream()
                .map(StudentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
        return StudentResponse.from(student);
    }

    /**
     * TODO-BE-001: Register a new student.
     * Rules:
     *   - email must be unique (throw BusinessRuleViolationException with a clear message)
     *   - new students start active
     *   - return the saved student as a StudentResponse
     *   - build the new entity with Student.builder() (project convention, see Student's Javadoc)
     * Think about: where should the transaction boundary be? Is the "check then save" sequence safe
     * under concurrency? (The unique column constraint is your safety net.)
     */
    public StudentResponse registerStudent(CreateStudentRequest request) {
        throw new UnsupportedOperationException("TODO-BE-001: implement student registration");
    }

    /**
     * TODO-BE-004: Search students by name fragment using the repository method you add.
     */
    public List<StudentResponse> searchStudents(String nameFragment) {
        throw new UnsupportedOperationException("TODO-BE-004: implement student search");
    }

    /**
     * TODO-BE-007: Activate or deactivate a student.
     * Throw ResourceNotFoundException if the student does not exist.
     * Hint to think about: do you need to call save() on an entity you just loaded inside a transaction?
     */
    public StudentResponse changeActiveStatus(Long id, boolean active) {
        throw new UnsupportedOperationException("TODO-BE-007: implement activate/deactivate");
    }
}
