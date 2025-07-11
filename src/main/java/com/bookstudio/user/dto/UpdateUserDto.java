package com.bookstudio.user.dto;

import com.bookstudio.shared.enums.Role;
import lombok.Data;

@Data
public class UpdateUserDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private Role role;
    private boolean deletePhoto;
    private String profilePhotoUrl;
}
