package com.bookstudio.shared.util;

import java.util.List;

import com.bookstudio.shared.response.OptionResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record SelectOptions(
        List<OptionResponse> books,
        List<OptionResponse> authors,
        List<OptionResponse> publishers,
        List<OptionResponse> categories,
        List<OptionResponse> students,
        List<OptionResponse> roles,
        List<OptionResponse> languages,
        List<OptionResponse> nationalities,
        List<OptionResponse> shelves
) {}
