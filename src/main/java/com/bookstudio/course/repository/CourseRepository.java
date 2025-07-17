package com.bookstudio.course.repository;

import com.bookstudio.course.model.Course;
import com.bookstudio.course.projection.CourseSelectProjection;
import com.bookstudio.course.projection.CourseViewProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("""
        SELECT
            c.id AS courseId,
            c.name AS name,
            c.level AS level,
            c.description AS description,
            c.status AS status
        FROM Course c
        ORDER BY c.id DESC
        """)
    List<CourseViewProjection> findList();

    @Query("""
        SELECT 
            c.id AS courseId,
            c.name AS name,
            c.level AS level,
            c.description AS description,
            c.status AS status
        FROM Course c
        WHERE c.id = :id
    """)
    Optional<CourseViewProjection> findInfoById(@Param("id") Long id);

    @Query("SELECT c.id as courseId, c.name as name FROM Course c WHERE c.status = 'activo'")
    List<CourseSelectProjection> findForSelect();
}
