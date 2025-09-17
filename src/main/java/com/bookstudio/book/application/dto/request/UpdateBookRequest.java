package com.bookstudio.book.application.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.bookstudio.shared.domain.model.type.Status;

import lombok.Data;

@Data
public class UpdateBookRequest {
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
