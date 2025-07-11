package com.bookstudio.author.projection;

import java.time.LocalDate;

import com.bookstudio.shared.util.IdFormatter;

public interface AuthorInfoProjection {
    Long getAuthorId();
    String getName();

    Long getNationalityId();
    String getNationalityName();

    Long getLiteraryGenreId();
    String getLiteraryGenreName();
    
    LocalDate getBirthDate();
    String getBiography();
    String getStatus();
    String getPhotoUrl();

    default String getFormattedAuthorId() {
        return IdFormatter.formatId(String.valueOf(getAuthorId()), "A");
    }
}
