package com.bookstudio.student.projection;

import com.bookstudio.shared.util.IdFormatter;

public interface StudentSelectProjection {
    Long getStudentId();
    String getFullName();

    default String getFormattedStudentId() {
        return IdFormatter.formatId(String.valueOf(getStudentId()), "ES");
    }
}
