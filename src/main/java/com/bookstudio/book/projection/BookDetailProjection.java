package com.bookstudio.book.projection;

import java.time.LocalDate;

import com.bookstudio.shared.util.IdFormatter;

public interface BookDetailProjection {
    Long getBookId();
    String getTitle();
    Integer getTotalCopies();
    Integer getAvailableCopies();
    Integer getLoanedCopies();
    LocalDate getReleaseDate();
    String getStatus();

    Long getAuthorId();
    String getAuthorName();

    Long getPublisherId();
    String getPublisherName();

    Long getCourseId();
    String getCourseName();

    Long getGenreId();
    String getGenreName();

    default String getFormattedBookId() {
        return IdFormatter.formatId(String.valueOf(getBookId()), "L");
    }

    default String getFormattedAuthorId() {
        return IdFormatter.formatId(String.valueOf(getAuthorId()), "A");
    }

    default String getFormattedPublisherId() {
        return IdFormatter.formatId(String.valueOf(getPublisherId()), "ED");
    }

    default String getFormattedCourseId() {
        return IdFormatter.formatId(String.valueOf(getCourseId()), "C");
    }
}
