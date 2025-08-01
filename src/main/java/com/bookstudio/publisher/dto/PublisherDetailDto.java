package com.bookstudio.publisher.dto;

import java.util.List;

import com.bookstudio.genre.dto.GenreSummaryDto;
import com.bookstudio.nationality.dto.NationalitySummaryDto;
import com.bookstudio.shared.enums.Status;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublisherDetailDto {
    private Long id;
    private String name;
    private NationalitySummaryDto nationality;
    private Integer foundationYear;
    private String website;
    private String address;
    private Status status;
    private String photoUrl;
    private List<GenreSummaryDto> genres;
}
