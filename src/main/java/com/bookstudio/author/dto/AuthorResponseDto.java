package com.bookstudio.author.dto;

import java.time.LocalDate;

import com.bookstudio.shared.util.IdFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorResponseDto {
    private Long authorId;
    private String name;
    private String nationalityName;
    private String literaryGenreName;
    private LocalDate birthDate;
    private String status;
    private String photoUrl;

    public String getFormattedAuthorId() {
        return IdFormatter.formatId(String.valueOf(authorId), "A");
    }
}
