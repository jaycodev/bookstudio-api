package com.bookstudio.course.dto;

import com.bookstudio.shared.util.IdFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseResponseDto {
    private Long courseId;
    private String name;
    private String level;
    private String description;
    private String status;

    public String getFormattedCourseId() {
        return IdFormatter.formatId(String.valueOf(courseId), "C");
    }
}
