package com.bookstudio.student.projection;

import java.time.LocalDate;

import com.bookstudio.shared.util.IdFormatter;

public interface StudentInfoProjection {
    Long getStudentId();
    String getDni();
    String getFirstName();
    String getLastName();
    String getAddress();
    String getPhone();
    String getEmail();
    LocalDate getBirthDate();
    String getGender();

    Long getFacultyId();
    String getFacultyName();

    String getStatus();

    default String getFormattedStudentId() {
        return IdFormatter.formatId(String.valueOf(getStudentId()), "ES");
    }
}
