package com.bookstudio.user.dto;

import com.bookstudio.shared.util.IdFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Base64;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private byte[] profilePhoto;

    public String getFormattedUserId() {
        return IdFormatter.formatId(String.valueOf(userId), "U");
    }

    public String getProfilePhotoBase64() {
        if (profilePhoto != null && profilePhoto.length > 0) {
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(profilePhoto);
        }
        return null;
    }
}
