package com.bookstudio.copy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CopyResponseDto {
    private Long copyId;
    private String code;
    private String bookTitle;
    private String shelfLocation;
    private Boolean isAvailable;
    private String condition;
}
