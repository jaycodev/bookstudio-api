package com.bookstudio.student.projection;

import com.bookstudio.shared.util.IdFormatter;

public interface StudentListProjection {
    Long getStudentId();
    String getDni();
    String getFirstName();
    String getLastName();
    String getPhone();
    String getEmail();
    String getStatus();

    default String getFormattedStudentId() {
        return IdFormatter.formatId(String.valueOf(getStudentId()), "ES");
    }
}
