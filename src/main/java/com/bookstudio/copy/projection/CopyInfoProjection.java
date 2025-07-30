package com.bookstudio.copy.projection;

public interface CopyInfoProjection {
    Long getCopyId();
    String getCode();
    String getBookName();
    String getShelfLocation();
    String getBarcode();
    Boolean getIsAvailable();
    String getCondition();
}
