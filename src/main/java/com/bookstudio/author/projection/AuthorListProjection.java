package com.bookstudio.author.projection;

import java.time.LocalDate;

public interface AuthorListProjection {
    Long getAuthorId();
    String getName();
    String getNationalityName();
    LocalDate getBirthDate();
    String getStatus();
    String getPhotoUrl();
}
