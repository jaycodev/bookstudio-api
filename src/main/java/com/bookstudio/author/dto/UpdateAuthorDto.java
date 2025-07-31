package com.bookstudio.author.dto;

import java.time.LocalDate;
import com.bookstudio.shared.enums.Status;
import lombok.Data;

@Data
public class UpdateAuthorDto {
    private Long id;
    private String name;
    private Long nationalityId;
    private LocalDate birthDate;
    private String biography;
    private Status status;
    private boolean deletePhoto;
    private String photoUrl;
}
