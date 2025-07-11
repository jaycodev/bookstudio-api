package com.bookstudio.book.projection;

import com.bookstudio.shared.util.IdFormatter;

public interface BookSelectProjection {
    Long getBookId();
    String getTitle();
    Integer getAvailableCopies();

    default String getFormattedBookId() {
        return IdFormatter.formatId(String.valueOf(getBookId()), "L");
    }
}
