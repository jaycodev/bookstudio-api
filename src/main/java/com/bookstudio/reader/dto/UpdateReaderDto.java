package com.bookstudio.reader.dto;

import java.time.LocalDate;

import com.bookstudio.reader.model.ReaderGender;
import com.bookstudio.reader.model.ReaderStatus;
import com.bookstudio.reader.model.ReaderType;
import lombok.Data;

@Data
public class UpdateReaderDto {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String email;
    private LocalDate birthDate;
    private ReaderGender gender;
    private ReaderType type;
    private ReaderStatus status;
}
