package com.bookstudio.course.repository;

import com.bookstudio.course.model.Course;
import com.bookstudio.shared.enums.Status;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByStatus(Status status);
}
