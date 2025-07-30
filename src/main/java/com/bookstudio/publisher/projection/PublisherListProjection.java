package com.bookstudio.publisher.projection;

public interface PublisherListProjection {
    Long getPublisherId();
    String getName();
    String getNationalityName();
    String getWebsite();
    String getStatus();
    String getPhotoUrl();
}
