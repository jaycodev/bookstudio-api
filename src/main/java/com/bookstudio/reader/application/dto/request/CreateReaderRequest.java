package com.bookstudio.reader.application.dto.request;

import java.time.LocalDate;

import com.bookstudio.reader.domain.model.type.ReaderGender;
import com.bookstudio.reader.domain.model.type.ReaderStatus;
import com.bookstudio.reader.domain.model.type.ReaderType;

import lombok.Data;

@Data
public class CreateReaderRequest {
    private String dni;
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
