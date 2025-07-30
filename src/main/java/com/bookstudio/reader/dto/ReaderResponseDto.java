package com.bookstudio.reader.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReaderResponseDto {
    private Long readerId;
    private String code;
    private String dni;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String status;
}
