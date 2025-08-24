package com.bookstudio.reader.dto;

import java.time.LocalDate;

import com.bookstudio.shared.enums.Gender;
import com.bookstudio.shared.enums.ReaderType;
import com.bookstudio.shared.enums.Status;
import lombok.Data;

@Data
public class UpdateReaderDto {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String email;
    private LocalDate birthDate;
    private Gender gender;
    private ReaderType type;
    private Status status;
}
