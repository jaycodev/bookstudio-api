package com.bookstudio.publisher.domain.dto.request;

import java.util.List;

import com.bookstudio.shared.domain.model.Status;

import lombok.Data;

@Data
public class UpdatePublisherRequest {
    private String name;
    private Long nationalityId;
    private Integer foundationYear;
    private String website;
    private String address;
    private Status status;
    private String photoUrl;
    private List<Long> genreIds;
}
