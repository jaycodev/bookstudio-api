package com.bookstudio.nationality.controller;

import com.bookstudio.nationality.dto.NationalityOptionDto;
import com.bookstudio.nationality.service.NationalityService;
import com.bookstudio.shared.util.ApiError;

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
            List<NationalityOptionDto> nationalities = nationalityService.getOptions();

            if (nationalities.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiError(false, "No nationalities found for filter.", "no_content", 204));
            }

            return ResponseEntity.ok(nationalities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(false, "Error fetching nationality filter options.", "server_error", 500));
        }
    }
}
