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

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @GetMapping
    public ResponseEntity<?> list() {
        List<LoanListResponse> loans = loanService.getList();
        if (loans.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiErrorResponse(false, "No loans found.", "no_content", 204));
        }
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{id}")
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
