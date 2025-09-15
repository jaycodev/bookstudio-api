package com.bookstudio.copy.domain.dto.request;

import com.bookstudio.copy.domain.model.CopyCondition;
import com.bookstudio.copy.domain.model.CopyStatus;

import lombok.Data;

@Data
public class CreateCopyRequest {
    private Long bookId;
    private Long shelfId;
    private String barcode;
    private CopyStatus status;
    private CopyCondition condition;
}
