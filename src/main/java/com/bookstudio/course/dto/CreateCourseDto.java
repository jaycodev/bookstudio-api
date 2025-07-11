package com.bookstudio.course.dto;

import com.bookstudio.shared.enums.Status;

import lombok.Data;

@Data
public class CreateCourseDto {
    private String name;
    private String level;
    private String description;
    private Status status;
}
