package com.bookstudio.publisher.dto;

import com.bookstudio.shared.util.IdFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Base64;

@Data
@AllArgsConstructor
public class PublisherResponseDto {
    private Long publisherId;
    private String name;
    private String nationalityName;
    private String literaryGenreName;
    private String website;
    private String status;
    private byte[] photo;

    public String getFormattedPublisherId() {
        return IdFormatter.formatId(String.valueOf(publisherId), "ED");
    }

    public String getPhotoBase64() {
        if (photo != null && photo.length > 0) {
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo);
        }
        return null;
    }
}
