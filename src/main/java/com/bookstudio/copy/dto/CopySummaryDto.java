package com.bookstudio.copy.dto;

import com.bookstudio.copy.model.CopyStatus;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CopySummaryDto {
    private Long id;
    private String code;
    private String barcode;
    private CopyStatus status;
}
