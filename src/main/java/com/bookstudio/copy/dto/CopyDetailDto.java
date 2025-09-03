package com.bookstudio.copy.dto;

import com.bookstudio.book.dto.BookSummaryDto;
import com.bookstudio.location.dto.ShelfSummaryDto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CopyDetailDto {
    private Long id;
    private String code;
    private BookSummaryDto book;
    private ShelfSummaryDto shelf;
    private String barcode;
    private String status;
    private String condition;
}
