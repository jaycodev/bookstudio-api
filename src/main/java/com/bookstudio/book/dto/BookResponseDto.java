package com.bookstudio.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookResponseDto {
    private Long bookId;
    private String title;
    private String isbn;
    private String coverUrl;
    private String publisherName;
    private String categoryName;
    private String status;
}
