package com.bookstudio.reader.domain.dto.response;

import java.time.LocalDate;

import com.bookstudio.reader.domain.model.ReaderGender;
import com.bookstudio.reader.domain.model.ReaderStatus;
import com.bookstudio.reader.domain.model.ReaderType;

public record ReaderDetailResponse(
        Long id,
        String code,
        String dni,
        String firstName,
        String lastName,
        String address,
        String phone,
        String email,
        LocalDate birthDate,
        ReaderGender gender,
        ReaderType type,
        ReaderStatus status) {
}
