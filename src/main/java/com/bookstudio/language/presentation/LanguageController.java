package com.bookstudio.language.presentation;

import com.bookstudio.language.application.LanguageService;
import com.bookstudio.shared.application.dto.response.ApiErrorResponse;
import com.bookstudio.shared.application.dto.response.OptionResponse;

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
            List<OptionResponse> languages = languageService.getOptions();

            if (languages.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiErrorResponse(false, "No languages found for filter.", "no_content", 204));
            }

            return ResponseEntity.ok(languages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, "Error fetching language filter options.", "server_error", 500));
        }
    }
}
