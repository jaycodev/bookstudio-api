package com.bookstudio.author.dto;

import java.time.LocalDate;
import com.bookstudio.shared.enums.Status;
import lombok.Data;

@Data
public class CreateAuthorDto {
    private String name;
    private Long nationalityId;
    private LocalDate birthDate;
    private String biography;
    private Status status;
    private String photoUrl;
}
