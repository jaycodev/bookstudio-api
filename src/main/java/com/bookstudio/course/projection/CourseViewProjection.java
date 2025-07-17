package com.bookstudio.course.projection;

import com.bookstudio.shared.util.IdFormatter;

public interface CourseViewProjection {
    Long getCourseId();
    String getName();
    String getLevel();
    String getDescription();
    String getStatus();

    default String getFormattedCourseId() {
        return IdFormatter.formatId(String.valueOf(getCourseId()), "C");
    }
}
