package com.bookstudio.student.dto;

import java.time.LocalDate;

import com.bookstudio.shared.enums.Status;
import lombok.Data;

@Data
public class CreateStudentDto {
    private String dni;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String email;
    private LocalDate birthDate;
    private String gender;
    private Long facultyId;
    private Status status;
}
