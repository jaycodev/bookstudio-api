package com.bookstudio.student.dto;

import com.bookstudio.shared.util.IdFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentResponseDto {
    private Long studentId;
    private String dni;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String status;

    public String getFormattedStudentId() {
        return IdFormatter.formatId(String.valueOf(studentId), "ES");
    }
}
