package com.bookstudio.publisher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublisherResponseDto {
    private Long publisherId;
    private String name;
    private String nationalityName;
    private String website;
    private String status;
    private String photoUrl;
}
