package com.bookstudio.book.projection;

import com.bookstudio.shared.util.IdFormatter;

public interface BookListProjection {
    Long getBookId();
    String getTitle();
    Integer getAvailableCopies();
    Integer getLoanedCopies();

    String getAuthorId();
    String getAuthorName();

    String getPublisherId();
    String getPublisherName();
    
    String getStatus();

    default String getFormattedBookId() {
        return IdFormatter.formatId(String.valueOf(getBookId()), "L");
    }

    default String getFormattedAuthorId() {
        return IdFormatter.formatId(String.valueOf(getAuthorId()), "A");
    }

    default String getFormattedPublisherId() {
        return IdFormatter.formatId(String.valueOf(getPublisherId()), "ED");
    }
}
