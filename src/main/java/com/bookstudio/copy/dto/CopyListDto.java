package com.bookstudio.copy.dto;

import com.bookstudio.shared.enums.Condition;

public record CopyListDto(
    String code,
    String bookTitle,
    String shelfLocation,
    Boolean isAvailable,
    Condition condition,
    Long id
) {}
