package com.bookstudio.book.projection;

import java.time.LocalDate;

public interface BookInfoProjection {
    Long getBookId();
    String getTitle();
    String getIsbn();
    String getLanguageCode();
    String getEdition();
    Integer getPages();
    String getDescription();
    String getCoverUrl();
    String getPublisherName();
    String getCategoryName();
    LocalDate getReleaseDate();
    String getStatus();
}
