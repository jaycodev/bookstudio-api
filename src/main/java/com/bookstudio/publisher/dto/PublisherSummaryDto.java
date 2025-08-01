package com.bookstudio.publisher.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublisherSummaryDto {
    private Long id;
    private String name;
}
