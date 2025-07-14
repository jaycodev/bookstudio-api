package com.bookstudio.author.projection;

import java.time.LocalDate;

import com.bookstudio.shared.util.IdFormatter;

public interface AuthorListProjection {
    Long getAuthorId();
    String getName();
    String getNationalityName();
    String getLiteraryGenreName();
    LocalDate getBirthDate();
    String getStatus();
    String getPhotoUrl();

    default String getFormattedAuthorId() {
        return IdFormatter.formatId(String.valueOf(getAuthorId()), "A");
    }
}
