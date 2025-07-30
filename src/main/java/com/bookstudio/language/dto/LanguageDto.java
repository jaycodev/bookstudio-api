package com.bookstudio.language.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LanguageDto {
    private Long id;
    private String name;
    private String code;
}
