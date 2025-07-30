package com.bookstudio.copy.projection;

public interface CopyListProjection {
    Long getCopyId();
    String getCode();
    String getBookName();
    String getShelfLocation();
    Boolean getIsAvailable();
    String getCondition();
}
