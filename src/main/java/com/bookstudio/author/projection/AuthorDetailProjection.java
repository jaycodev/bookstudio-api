package com.bookstudio.author.projection;

import java.time.LocalDate;
import java.util.Base64;

import com.bookstudio.shared.util.IdFormatter;

public interface AuthorDetailProjection {
    Long getAuthorId();
    String getName();

    Long getNationalityId();
    String getNationalityName();

    Long getLiteraryGenreId();
    String getLiteraryGenreName();
    
    LocalDate getBirthDate();
    String getBiography();
    String getStatus();
    byte[] getPhoto();

    default String getFormattedAuthorId() {
        return IdFormatter.formatId(String.valueOf(getAuthorId()), "A");
    }

    default String getPhotoBase64() {
        byte[] photoBytes = getPhoto();
        if (photoBytes != null && photoBytes.length > 0) {
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photoBytes);
        }
        return null;
    }
}
