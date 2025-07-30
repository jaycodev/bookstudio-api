package com.bookstudio.author.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorResponseDto {
    private Long authorId;
    private String name;
    private String nationalityName;
    private LocalDate birthDate;
    private String status;
    private String photoUrl;
}
