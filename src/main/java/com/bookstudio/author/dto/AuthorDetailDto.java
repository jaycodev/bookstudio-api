package com.bookstudio.author.dto;

import java.time.LocalDate;

import com.bookstudio.nationality.dto.NationalitySummaryDto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDetailDto {
    private Long id;
    private String name;
    private NationalitySummaryDto nationality;
    private LocalDate birthDate;
    private String biography;
    private String status;
    private String photoUrl;
}
