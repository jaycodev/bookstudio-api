package com.bookstudio.author.projection;

import com.bookstudio.shared.util.IdFormatter;

public interface AuthorSelectProjection {
    Long getAuthorId();
    String getName();

    default String getFormattedAuthorId() {
        return IdFormatter.formatId(String.valueOf(getAuthorId()), "A");
    }
}
