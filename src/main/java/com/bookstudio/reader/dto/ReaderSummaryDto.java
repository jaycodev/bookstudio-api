package com.bookstudio.reader.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReaderSummaryDto {
    private Long id;
    private String code;
    private String firstName;
    private String lastName;
}
