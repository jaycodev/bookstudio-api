package com.bookstudio.course.service;

import com.bookstudio.course.model.Course;
import com.bookstudio.course.projection.CourseSelectProjection;
import com.bookstudio.course.repository.CourseRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> list() {
        return courseRepository.findAll();
    }

    public Optional<Course> findById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    @Transactional
    public Course create(Course course) {
        return courseRepository.save(course);
    }

    @Transactional
    public Course update(Course updatedCourse) {
        return courseRepository.findById(updatedCourse.getId()).map(course -> {
            course.setName(updatedCourse.getName());
            course.setLevel(updatedCourse.getLevel());
            course.setDescription(updatedCourse.getDescription());
            course.setStatus(updatedCourse.getStatus());
            return courseRepository.save(course);
        }).orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + updatedCourse.getId()));
    }

    public List<CourseSelectProjection> getForSelect() {
        return courseRepository.findForSelect();
    }
}
