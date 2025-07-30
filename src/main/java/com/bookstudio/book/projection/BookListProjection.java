package com.bookstudio.book.projection;

public interface BookListProjection {
    Long getBookId();
    String getTitle();
    String getIsbn();
    String getCoverUrl();
    String getPublisherName();
    String getCategoryName();
    String getStatus();
}
