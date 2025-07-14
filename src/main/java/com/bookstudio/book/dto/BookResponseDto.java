package com.bookstudio.book.dto;

import com.bookstudio.shared.util.IdFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookResponseDto {
    private Long bookId;
    private String title;
    private Integer availableCopies;
    private Integer loanedCopies;

    private String authorId;
    private String authorName;

    private String publisherId;
    private String publisherName;

    private String status;

    public String getFormattedBookId() {
        return IdFormatter.formatId(String.valueOf(bookId), "L");
    }

    public String getFormattedAuthorId() {
        return IdFormatter.formatId(authorId, "A");
    }

    public String getFormattedPublisherId() {
        return IdFormatter.formatId(publisherId, "ED");
    }
}
