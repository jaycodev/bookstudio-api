package com.bookstudio.reader.domain.dto.response;

import com.bookstudio.reader.domain.model.ReaderStatus;
import com.bookstudio.reader.domain.model.ReaderType;

public record ReaderListResponse(
        Long id,
        String code,
        String fullName,
        String phone,
        String email,
        ReaderType type,
        ReaderStatus status) {
}