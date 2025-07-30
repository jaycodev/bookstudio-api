package com.bookstudio.reader.projection;

import java.time.LocalDate;

public interface ReaderInfoProjection {
    Long getReaderId();
    String getCode();
    String getDni();
    String getFirstName();
    String getLastName();
    String getAddress();
    String getPhone();
    String getEmail();
    LocalDate getBirthDate();
    String getGender();
    String getStatus();
}
