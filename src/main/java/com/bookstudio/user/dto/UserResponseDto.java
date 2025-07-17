package com.bookstudio.user.dto;

import com.bookstudio.shared.util.IdFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String profilePhotoUrl;

    public String getFormattedUserId() {
        return IdFormatter.formatId(String.valueOf(userId), "U");
    }
}
