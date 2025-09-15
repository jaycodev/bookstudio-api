package com.bookstudio.author.domain.dto.request;

import java.time.LocalDate;

import com.bookstudio.shared.domain.model.Status;

import lombok.Data;

@Data
public class UpdateAuthorRequest {
    private String name;
    private Long nationalityId;
    private LocalDate birthDate;
    private String biography;
    private Status status;
    private String photoUrl;
}
