package com.bookstudio.copy.dto;

import com.bookstudio.shared.enums.Condition;

public record CopyListDto(
    Long id,
    String code,
    String bookCoverUrl,
    String bookTitle,
    String shelfCode,
    String shelfFloor,
    String locationName,
    Boolean isAvailable,
    Condition condition
) {}
