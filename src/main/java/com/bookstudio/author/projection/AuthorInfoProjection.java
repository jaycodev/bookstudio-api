package com.bookstudio.author.projection;

import java.time.LocalDate;

public interface AuthorInfoProjection {
    Long getAuthorId();
    String getName();

    Long getNationalityId();
    String getNationalityName();
    
    LocalDate getBirthDate();
    String getBiography();
    String getStatus();
    String getPhotoUrl();
}
