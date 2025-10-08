package com.bookstudio.copy.application.dto.request;

import com.bookstudio.copy.domain.model.type.CopyCondition;
import com.bookstudio.copy.domain.model.type.CopyStatus;

public record UpdateCopyRequest(
    Long shelfId,
    String barcode,
    CopyStatus status,
    CopyCondition condition
) {}
