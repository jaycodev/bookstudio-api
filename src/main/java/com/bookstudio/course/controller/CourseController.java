package com.bookstudio.course.controller;

import com.bookstudio.course.dto.CourseResponseDto;
import com.bookstudio.course.dto.CreateCourseDto;
import com.bookstudio.course.dto.UpdateCourseDto;
import com.bookstudio.course.projection.CourseViewProjection;
import com.bookstudio.course.service.CourseService;
import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<?> list() {
        List<CourseViewProjection> courses = courseService.getList();
        if (courses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiError(false, "No courses found.", "no_content", 204));
        }
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        CourseViewProjection course = courseService.getInfoById(id).orElse(null);
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, "Course not found.", "not_found", 404));
        }
        return ResponseEntity.ok(course);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateCourseDto dto) {
        try {
            CourseResponseDto created = courseService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiError(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateCourseDto dto) {
        try {
            CourseResponseDto updated = courseService.update(dto);
            return ResponseEntity.ok(new ApiResponse(true, updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, e.getMessage(), "update_failed", 404));
        }
    }
}
