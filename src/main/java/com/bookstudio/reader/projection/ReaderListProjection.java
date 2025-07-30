package com.bookstudio.reader.projection;

public interface ReaderListProjection {
    Long getReaderId();
    String getCode();
    String getDni();
    String getFirstName();
    String getLastName();
    String getPhone();
    String getEmail();
    String getStatus();
}
