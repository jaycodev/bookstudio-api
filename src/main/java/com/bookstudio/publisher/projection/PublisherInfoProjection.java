package com.bookstudio.publisher.projection;

import java.util.Base64;

import com.bookstudio.shared.util.IdFormatter;

public interface PublisherInfoProjection {
    Long getPublisherId();
    String getName();

    Long getNationalityId();
    String getNationalityName();

    Long getLiteraryGenreId();
    String getLiteraryGenreName();

    Integer getFoundationYear();
    String getWebsite();
    String getAddress();
    String getStatus();

    byte[] getPhoto();

    default String getFormattedPublisherId() {
        return IdFormatter.formatId(String.valueOf(getPublisherId()), "ED");
    }

    default String getPhotoBase64() {
        byte[] photoBytes = getPhoto();
        if (photoBytes != null && photoBytes.length > 0) {
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photoBytes);
        }
        return null;
    }
}
