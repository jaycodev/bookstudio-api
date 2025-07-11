package com.bookstudio.publisher.dto;

import com.bookstudio.shared.util.IdFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublisherResponseDto {
    private Long publisherId;
    private String name;
    private String nationalityName;
    private String literaryGenreName;
    private String website;
    private String status;
    private String photoUrl;

    public String getFormattedPublisherId() {
        return IdFormatter.formatId(String.valueOf(publisherId), "ED");
    }
}
