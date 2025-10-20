package com.bookstudio.nationality.presentation;

import com.bookstudio.nationality.application.NationalityService;
import com.bookstudio.shared.api.ApiError;
import com.bookstudio.shared.api.ApiSuccess;
import com.bookstudio.shared.response.OptionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/nationalities")
@RequiredArgsConstructor
@Tag(name = "Nationalities", description = "Operations related to nationalities")
public class NationalityController {
    private final NationalityService nationalityService;

    @GetMapping("/filter-options")
    @Operation(summary = "Get nationality filter options")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filter options retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No nationalities found for filter"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiError.class), examples = @ExampleObject(name = "Internal Error", summary = "Internal server error", value = "{\"success\":false,\"status\":500,\"message\":\"Internal server error\",\"path\":\"/nationalities/filter-options\",\"timestamp\":\"2025-10-16T21:09:26.122Z\",\"errors\":null}")))
    })
    public ResponseEntity<ApiSuccess<List<OptionResponse>>> filterOptions() {
        List<OptionResponse> nationalities = nationalityService.getOptions();
        ApiSuccess<List<OptionResponse>> response = new ApiSuccess<>(
                nationalities.isEmpty() ? "No nationalities found for filter" : "Filter options retrieved successfully",
                nationalities);

        HttpStatus status = nationalities.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }
}
