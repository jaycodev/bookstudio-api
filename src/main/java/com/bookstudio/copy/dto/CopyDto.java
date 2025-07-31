package com.bookstudio.copy.dto;

import com.bookstudio.book.dto.BookDto;
import com.bookstudio.location.dto.ShelfDto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CopyDto {
    private Long id;
    private String code;
    private BookDto book;
    private ShelfDto shelf;
    private String barcode;
    private Boolean isAvailable;
    private String condition;
}
