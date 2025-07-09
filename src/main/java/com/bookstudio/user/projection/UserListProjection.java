package com.bookstudio.user.projection;

import java.util.Base64;

import com.bookstudio.shared.util.IdFormatter;

public interface UserListProjection {
    Long getUserId();
    String getUsername();
    String getEmail();
    String getFirstName();
    String getLastName();
    String getRole();
    byte[] getProfilePhoto();

    default String getFormattedUserId() {
        return IdFormatter.formatId(String.valueOf(getUserId()), "U");
    }

    default String getProfilePhotoBase64() {
        byte[] photoBytes = getProfilePhoto();
        if (photoBytes != null && photoBytes.length > 0) {
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photoBytes);
        }
        return null;
    }
}
