package com.bookstudio.course.projection;

import com.bookstudio.shared.util.IdFormatter;

public interface CourseSelectProjection {
    Long getCourseId();
    String getName();

    default String getFormattedCourseId() {
        return IdFormatter.formatId(String.valueOf(getCourseId()), "C");
    }
}
