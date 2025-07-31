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
    private NationalityDto nationality;
    private LocalDate birthDate;
    private String biography;
    private String status;
    private String photoUrl;
}
