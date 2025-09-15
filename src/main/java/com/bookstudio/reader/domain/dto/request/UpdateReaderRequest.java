package com.bookstudio.reader.domain.dto.request;

import java.time.LocalDate;

import com.bookstudio.reader.domain.model.ReaderGender;
import com.bookstudio.reader.domain.model.ReaderStatus;
import com.bookstudio.reader.domain.model.ReaderType;

import lombok.Data;

@Data
public class UpdateReaderRequest {
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
