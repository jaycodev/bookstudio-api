package com.bookstudio.worker.application.dto.response;

import java.util.List;

import com.bookstudio.shared.response.OptionResponse;

public record WorkerSelectOptionsResponse(
        List<OptionResponse> roles) {
}
