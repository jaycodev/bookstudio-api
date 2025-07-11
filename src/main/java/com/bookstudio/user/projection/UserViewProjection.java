package com.bookstudio.user.projection;

import com.bookstudio.shared.util.IdFormatter;

public interface UserViewProjection {
    Long getUserId();
    String getUsername();
    String getEmail();
    String getFirstName();
    String getLastName();
    String getRole();
    String getProfilePhotoUrl();

    default String getFormattedUserId() {
        return IdFormatter.formatId(String.valueOf(getUserId()), "U");
    }
}
