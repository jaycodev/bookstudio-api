package com.bookstudio.publisher.projection;

public interface PublisherInfoProjection {
    Long getPublisherId();
    String getName();

    Long getNationalityId();
    String getNationalityName();

    Integer getFoundationYear();
    String getWebsite();
    String getAddress();
    String getStatus();
    String getPhotoUrl();
}
