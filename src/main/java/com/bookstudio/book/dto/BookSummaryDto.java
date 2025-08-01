package com.bookstudio.book.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookSummaryDto {
    private Long id;
    private String title;
    private String isbn;
}
