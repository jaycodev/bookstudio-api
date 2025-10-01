package com.bookstudio.loan.presentation;

import com.bookstudio.loan.application.LoanService;
import com.bookstudio.loan.application.dto.request.CreateLoanRequest;
import com.bookstudio.loan.application.dto.request.UpdateLoanRequest;
import com.bookstudio.loan.application.dto.response.LoanDetailResponse;
import com.bookstudio.loan.application.dto.response.LoanListResponse;
import com.bookstudio.shared.application.dto.response.ApiErrorResponse;
import com.bookstudio.shared.application.dto.response.ApiResponse;
import com.bookstudio.shared.application.dto.response.OptionResponse;
import com.bookstudio.shared.util.SelectOptions;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
@Tag(name = "Loans", description = "Operations related to loans")
public class LoanController {

    private final LoanService loanService;

    @GetMapping
    @Operation(summary = "List all loans")
    public ResponseEntity<?> list() {
        List<LoanListResponse> loans = loanService.getList();
        if (loans.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No loans found.", "no_content", 204));
        }
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a loan by ID")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            LoanDetailResponse loan = loanService.getDetailById(id);
            return ResponseEntity.ok(loan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, "Loan not found.", "not_found", 404));
        }
    }

    @PostMapping
    @Operation(summary = "Create a new loan")
    public ResponseEntity<?> create(@RequestBody CreateLoanRequest request) {
        try {
            LoanListResponse created = loanService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponse(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a loan by ID")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateLoanRequest request) {
        try {
            LoanListResponse result = loanService.update(id, request);
            return ResponseEntity.ok(new ApiResponse(true, result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponse(false, e.getMessage(), "update_failed", 404));
        }
    }

    @GetMapping("/filter-options")
    @Operation(summary = "Get loan filter options")
    public ResponseEntity<?> filterOptions() {
        try {
            List<OptionResponse> loans = loanService.getOptions();
            if (loans.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiErrorResponse(false, "No loans found for filter.", "no_content", 204));
            }
            return ResponseEntity.ok(loans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, "Error fetching loan filter options.", "server_error", 500));
        }
    }

    @GetMapping("/select-options")
    @Operation(summary = "Get select options for loans")
    public ResponseEntity<?> selectOptions() {
        try {
            SelectOptions options = loanService.getSelectOptions();
            if (options == null || (options.getBooks() == null && options.getStudents() == null)) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiErrorResponse(false, "No select options found.", "no_content", 204));
            }
            return ResponseEntity.ok(options);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(false, "Error populating select options.", "server_error", 500));
        }
    }
}
