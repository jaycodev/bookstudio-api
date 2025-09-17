package com.bookstudio.shared.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import com.bookstudio.shared.util.FieldErrorDetail;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {
    private boolean success;
    private String errorType;
    private int statusCode;
    private List<FieldErrorDetail> errors;
}
