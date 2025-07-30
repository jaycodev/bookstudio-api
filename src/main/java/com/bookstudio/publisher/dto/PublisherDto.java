package com.bookstudio.publisher.dto;

import com.bookstudio.nationality.dto.NationalityDto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublisherDto {
    private Long id;
    private String name;
    private NationalityDto nationality;
    private Integer foundationYear;
    private String website;
    private String address;
    private String status;
    private String photoUrl;
}
