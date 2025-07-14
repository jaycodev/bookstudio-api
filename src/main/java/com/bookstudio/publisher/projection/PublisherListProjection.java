package com.bookstudio.publisher.projection;

import com.bookstudio.shared.util.IdFormatter;

public interface PublisherListProjection {
    Long getPublisherId();
    String getName();
    String getNationalityName();
    String getLiteraryGenreName();
    String getWebsite();
    String getStatus();
    String getPhotoUrl();

    default String getFormattedPublisherId() {
        return IdFormatter.formatId(String.valueOf(getPublisherId()), "ED");
    }
}
