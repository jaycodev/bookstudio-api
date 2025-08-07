package com.bookstudio.copy.dto;

import com.bookstudio.shared.enums.Condition;

public record CopyListDto(
    String code,
    String bookTitle,
    String shelfCode,
    String shelfFloor,
    String locationName,
    Boolean isAvailable,
    Condition condition,
    Long id
) {}
