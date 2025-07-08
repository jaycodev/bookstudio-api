package com.bookstudio.shared.util;

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
