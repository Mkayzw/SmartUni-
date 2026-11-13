package com.smartuni.course;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCode(String code);

    List<Course> findAllByOrderByCodeAsc();
}
