package com.bookstudio.publisher.dto;

import java.util.List;

import com.bookstudio.shared.enums.Status;
import lombok.Data;

@Data
public class CreatePublisherDto {
    private String name;
    private Long nationalityId;
    private Integer foundationYear;
    private String website;
    private String address;
    private Status status;
    private String photoUrl;
    private List<Long> genreIds;
}
