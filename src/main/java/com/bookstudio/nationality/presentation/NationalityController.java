package com.bookstudio.nationality.presentation;

import com.bookstudio.nationality.application.NationalityService;
import com.bookstudio.shared.domain.dto.response.ApiErrorResponse;
import com.bookstudio.shared.domain.dto.response.OptionResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nationalities")
@RequiredArgsConstructor
public class NationalityController {

    private final NationalityService nationalityService;

    @GetMapping("/filter-options")
    public ResponseEntity<?> filterOptions() {
        try {
            List<OptionResponse> nationalities = nationalityService.getOptions();

            if (nationalities.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiErrorResponse(false, "No nationalities found for filter.", "no_content", 204));
            }

            return ResponseEntity.ok(nationalities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, "Error fetching nationality filter options.", "server_error", 500));
        }
    }
}
