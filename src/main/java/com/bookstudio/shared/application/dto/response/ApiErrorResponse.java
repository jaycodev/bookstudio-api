package com.bookstudio.shared.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private boolean success;
    private String message;
    private String errorType;
    private int statusCode;
}
