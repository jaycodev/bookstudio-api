package com.bookstudio.copy.dto;

import com.bookstudio.copy.model.CopyCondition;
import com.bookstudio.copy.model.CopyStatus;

import lombok.Data;

@Data
public class UpdateCopyDto {
    private Long shelfId;
    private String barcode;
    private CopyStatus status;
    private CopyCondition condition;
}
