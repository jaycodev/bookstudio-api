package com.bookstudio.book.dto;

import java.time.LocalDate;
import com.bookstudio.shared.enums.Status;
import lombok.Data;

@Data
public class CreateBookDto {
    private String title;
    private String isbn;
    private Long languageId;
    private String edition;
    private Integer pages;
    private String description;
    private String coverUrl;
    private Long publisherId;
    private Long categoryId;
    private LocalDate releaseDate;
    private Status status;
}
