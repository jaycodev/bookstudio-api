package com.bookstudio.publisher.projection;

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
    String getPhotoUrl();

    default String getFormattedPublisherId() {
        return IdFormatter.formatId(String.valueOf(getPublisherId()), "ED");
    }
}
