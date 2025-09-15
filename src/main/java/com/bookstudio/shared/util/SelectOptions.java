package com.bookstudio.shared.util;

import java.util.List;

import com.bookstudio.shared.domain.dto.response.OptionResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectOptions {
    private List<OptionResponse> books;
    private List<OptionResponse> authors;
    private List<OptionResponse> publishers;
    private List<OptionResponse> categories;
    private List<OptionResponse> students;
    private List<OptionResponse> roles;
    private List<OptionResponse> languages;
    private List<OptionResponse> nationalities;
    private List<OptionResponse> shelves;
}
