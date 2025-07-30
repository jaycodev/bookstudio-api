package com.bookstudio.author.dto;

import java.time.LocalDate;

import com.bookstudio.nationality.dto.NationalityDto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {
    private Long id;
    private String name;
    private String biography;
    private LocalDate birthDate;
    private String photoUrl;
    private String status;
    private NationalityDto nationality;
}
