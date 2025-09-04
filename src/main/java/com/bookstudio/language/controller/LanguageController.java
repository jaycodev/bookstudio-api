package com.bookstudio.language.controller;

import com.bookstudio.language.dto.LanguageOptionDto;
import com.bookstudio.language.service.LanguageService;
import com.bookstudio.shared.util.ApiError;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping("/filter-options")
    public ResponseEntity<?> filterOptions() {
        try {
            List<LanguageOptionDto> languages = languageService.getOptions();

            if (languages.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiError(false, "No languages found for filter.", "no_content", 204));
            }

            return ResponseEntity.ok(languages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(false, "Error fetching language filter options.", "server_error", 500));
        }
    }
}
