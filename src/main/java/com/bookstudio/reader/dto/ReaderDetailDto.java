package com.bookstudio.reader.dto;

import java.time.LocalDate;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReaderDetailDto {
    private Long id;
    private String code;
    private String dni;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String email;
    private LocalDate birthDate;
    private String gender;
    private String type;
    private String status;
}
