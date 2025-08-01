package com.bookstudio.author.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorSummaryDto {
    private Long id;
    private String name;
}