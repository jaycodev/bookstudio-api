package com.bookstudio.book.dto;

import java.time.LocalDate;
import java.util.List;

import com.bookstudio.shared.enums.Status;
import lombok.Data;

@Data
public class UpdateBookDto {
    private Long id;
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
    private List<Long> authorIds;
    private List<Long> genreIds;
}
