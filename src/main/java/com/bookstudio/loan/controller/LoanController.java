package com.bookstudio.loan.controller;

import com.bookstudio.loan.dto.CreateLoanDto;
import com.bookstudio.loan.dto.LoanDetailDto;
import com.bookstudio.loan.dto.LoanListDto;
import com.bookstudio.loan.dto.LoanOptionDto;
import com.bookstudio.loan.dto.UpdateLoanDto;
import com.bookstudio.loan.service.LoanService;
import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;
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
        List<LoanListDto> loans = loanService.getList();
        if (loans.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiError(false, "No loans found.", "no_content", 204));
        }
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            LoanDetailDto loan = loanService.getInfoById(id);
            return ResponseEntity.ok(loan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, "Loan not found.", "not_found", 404));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateLoanDto dto) {
        try {
            LoanListDto created = loanService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiError(false, e.getMessage(), "creation_failed", 400));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateLoanDto dto) {
        try {
            LoanListDto result = loanService.update(id, dto);
            return ResponseEntity.ok(new ApiResponse(true, result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(false, e.getMessage(), "update_failed", 404));
        }
    }

    @GetMapping("/filter-options")
    public ResponseEntity<?> filterOptions() {
        try {
            List<LoanOptionDto> loans = loanService.getOptions();

            if (loans.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiError(false, "No loans found for filter.", "no_content", 204));
            }

            return ResponseEntity.ok(loans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(false, "Error fetching loan filter options.", "server_error", 500));
        }
    }

    @GetMapping("/select-options")
    public ResponseEntity<?> selectOptions() {
        try {
            SelectOptions options = loanService.getSelectOptions();
            if (options == null || (options.getBooks() == null && options.getStudents() == null)) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiError(false, "No select options found.", "no_content", 204));
            }
            return ResponseEntity.ok(options);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(false, "Error populating select options.", "server_error", 500));
        }
    }
}
