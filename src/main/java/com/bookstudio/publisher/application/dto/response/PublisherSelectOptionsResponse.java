package com.bookstudio.publisher.application.dto.response;

import java.util.List;

import com.bookstudio.shared.response.OptionResponse;

public record PublisherSelectOptionsResponse(
        List<OptionResponse> nationalities) {
}
