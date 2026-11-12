package com.smartuni.student;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Student> findAllByOrderByLastNameAscFirstNameAsc();

    // TODO-BE-004: add a derived query method for searching students.
    //   Requirement: case-insensitive "contains" match against first name OR last name.
    //   Think about: the method name, the parameter(s), and whether you need one parameter or two.
}
