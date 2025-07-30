package com.bookstudio.publisher.dto;

import com.bookstudio.shared.enums.Status;
import lombok.Data;

@Data
public class UpdatePublisherDto {
    private Long publisherId;
    private String name;
    private Long nationalityId;
    private Integer foundationYear;
    private String website;
    private String address;
    private Status status;
    private boolean deletePhoto;
    private String photoUrl;
}
