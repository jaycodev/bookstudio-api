package com.bookstudio.nationality.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NationalityDto {
    private Long id;
    private String name;
}
