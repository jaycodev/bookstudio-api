package com.bookstudio.reader.dto;

import com.bookstudio.shared.enums.ReaderType;
import com.bookstudio.shared.enums.Status;

public record ReaderListDto(
    Long id,
    String code,
    String fullName,
    String dni,
    String phone,
    String email,
    ReaderType type,
    Status status
) {}