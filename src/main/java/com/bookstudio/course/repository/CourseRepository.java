package com.bookstudio.course.repository;

import com.bookstudio.course.model.Course;
import com.bookstudio.course.projection.CourseSelectProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c.id as courseId, c.name as name FROM Course c WHERE c.status = 'activo'")
    List<CourseSelectProjection> findForSelect();
}
