package com.bookstudio.shared.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {
    private boolean success;
    private String message;
    private String errorType;
    private int statusCode;
}
