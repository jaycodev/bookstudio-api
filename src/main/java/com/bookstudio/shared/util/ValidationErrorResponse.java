package com.bookstudio.shared.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {
    private boolean success;
    private String errorType;
    private int statusCode;
    private List<FieldErrorDetail> errors;
}
