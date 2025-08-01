package com.bookstudio.language.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LanguageSummaryDto {
    private Long id;
    private String name;
    private String code;
}
