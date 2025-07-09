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

	public List<Course> listCourses() {
		return courseRepository.findAll();
	}

	public Optional<Course> getCourse(Long courseId) {
		return courseRepository.findById(courseId);
	}

	@Transactional
	public Course createCourse(Course course) {
		return courseRepository.save(course);
	}

	@Transactional
	public Course updateCourse(Long courseId, Course updatedData) {
		return courseRepository.findById(courseId).map(course -> {
			course.setName(updatedData.getName());
			course.setLevel(updatedData.getLevel());
			course.setDescription(updatedData.getDescription());
			course.setStatus(updatedData.getStatus());
			return courseRepository.save(course);
		}).orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + courseId));
	}

	public List<CourseSelectProjection> getCoursesForSelect() {
		return courseRepository.findForSelect();
	}
}
