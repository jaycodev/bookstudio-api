package com.bookstudio.user.dto;

import com.bookstudio.shared.enums.Role;
import lombok.Data;

@Data
public class CreateUserDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Role role;
    private String profilePhotoUrl;
}
