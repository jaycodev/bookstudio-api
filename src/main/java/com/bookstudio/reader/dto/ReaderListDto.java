package com.bookstudio.reader.dto;

import com.bookstudio.reader.model.ReaderStatus;
import com.bookstudio.reader.model.ReaderType;

public record ReaderListDto(
    Long id,
    String code,
    String fullName,
    String dni,
    String phone,
    String email,
    ReaderType type,
    ReaderStatus status
) {}