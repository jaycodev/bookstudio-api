package com.bookstudio.course.service;

import com.bookstudio.course.dto.CourseResponseDto;
import com.bookstudio.course.dto.CreateCourseDto;
import com.bookstudio.course.dto.UpdateCourseDto;
import com.bookstudio.course.model.Course;
import com.bookstudio.course.projection.CourseSelectProjection;
import com.bookstudio.course.projection.CourseViewProjection;
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

    public List<CourseViewProjection> getList() {
        return courseRepository.findList();
    }

    public Optional<Course> findById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    public Optional<CourseViewProjection> getInfoById(Long courseId) {
        return courseRepository.findInfoById(courseId);
    }

    @Transactional
    public CourseResponseDto create(CreateCourseDto dto) {
        Course course = new Course();
        course.setName(dto.getName());
        course.setLevel(dto.getLevel());
        course.setDescription(dto.getDescription());
        course.setStatus(dto.getStatus());

        Course saved = courseRepository.save(course);

        return new CourseResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getLevel(),
                saved.getDescription(),
                saved.getStatus().name());
    }

    @Transactional
    public CourseResponseDto update(UpdateCourseDto dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + dto.getCourseId()));

        course.setName(dto.getName());
        course.setLevel(dto.getLevel());
        course.setDescription(dto.getDescription());
        course.setStatus(dto.getStatus());

        Course updated = courseRepository.save(course);

        return new CourseResponseDto(
                updated.getId(),
                updated.getName(),
                updated.getLevel(),
                updated.getDescription(),
                updated.getStatus().name());
    }

    public List<CourseSelectProjection> getForSelect() {
        return courseRepository.findForSelect();
    }
}
