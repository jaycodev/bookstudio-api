package com.bookstudio.shared.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    private boolean success;
    private Object data;

    public ApiResponse(boolean success) {
        this.success = success;
        this.data = null;
    }
}
