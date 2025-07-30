package com.bookstudio.copy.dto;

import com.bookstudio.shared.enums.Condition;
import lombok.Data;

@Data
public class UpdateCopyDto {
    private Long copyId;
    private Long shelfId;
    private String barcode;
    private Boolean isAvailable;
    private Condition condition;
}
