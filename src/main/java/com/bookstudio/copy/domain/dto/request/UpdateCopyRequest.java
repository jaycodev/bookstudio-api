package com.bookstudio.copy.domain.dto.request;

import com.bookstudio.copy.domain.model.CopyCondition;
import com.bookstudio.copy.domain.model.CopyStatus;

import lombok.Data;

@Data
public class UpdateCopyRequest {
    private Long shelfId;
    private String barcode;
    private CopyStatus status;
    private CopyCondition condition;
}
