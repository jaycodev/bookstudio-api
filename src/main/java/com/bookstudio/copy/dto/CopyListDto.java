package com.bookstudio.copy.dto;

import com.bookstudio.copy.model.CopyCondition;
import com.bookstudio.copy.model.CopyStatus;

public record CopyListDto(
    Long id,
    String code,
    Long bookId,
    String bookCoverUrl,
    String bookTitle,
    String shelfCode,
    String shelfFloor,
    String locationName,
    CopyStatus status,
    CopyCondition condition
) {}
