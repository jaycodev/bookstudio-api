package com.bookstudio.book.dto;

import java.time.LocalDate;
import com.bookstudio.shared.enums.Status;
import lombok.Data;

@Data
public class CreateBookDto {
    private String title;
    private int totalCopies;
    private Long authorId;
    private Long publisherId;
    private Long courseId;
    private Long genreId;
    private LocalDate releaseDate;
    private Status status;
}
