package com.bookstudio.publisher.projection;

import com.bookstudio.shared.util.IdFormatter;

public interface PublisherSelectProjection {
    Long getPublisherId();
    String getName();

    default String getFormattedPublisherId() {
        return IdFormatter.formatId(String.valueOf(getPublisherId()), "ED");
    }
}
