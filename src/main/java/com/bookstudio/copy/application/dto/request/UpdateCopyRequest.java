package com.bookstudio.copy.application.dto.request;

import com.bookstudio.copy.domain.model.type.CopyCondition;
import com.bookstudio.copy.domain.model.type.CopyStatus;

import lombok.Data;

@Data
public class UpdateCopyRequest {
    private Long shelfId;
    private String barcode;
    private CopyStatus status;
    private CopyCondition condition;
}
