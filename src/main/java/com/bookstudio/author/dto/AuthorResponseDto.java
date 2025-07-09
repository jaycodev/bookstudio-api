package com.bookstudio.author.dto;

import java.time.LocalDate;
import java.util.Base64;

import com.bookstudio.shared.util.IdFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorResponseDto {
    private Long authorId;
    private String name;
    private String nationalityName;
    private String genreName;
    private LocalDate birthDate;
    private String status;
    private byte[] photo;

    public String getFormattedAuthorId() {
        return IdFormatter.formatId(String.valueOf(authorId), "A");
    }

    public String getPhotoBase64() {
        if (photo != null && photo.length > 0) {
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo);
        }
        return null;
    }
}
