package com.bookstudio.author.dto;

import java.time.LocalDate;
import com.bookstudio.shared.enums.Status;
import lombok.Data;

@Data
public class UpdateAuthorDto {
    private Long authorId;
    private String name;
    private Long nationalityId;
    private Long literaryGenreId;
    private LocalDate birthDate;
    private String biography;
    private Status status;
    private Boolean deletePhoto;
    private byte[] photo;
}
