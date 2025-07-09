package com.bookstudio.loan.controller;

import com.bookstudio.loan.dto.CreateLoanDto;
import com.bookstudio.loan.dto.LoanResponseDto;
import com.bookstudio.loan.dto.UpdateLoanDto;
import com.bookstudio.loan.projection.LoanInfoProjection;
import com.bookstudio.loan.projection.LoanListProjection;
import com.bookstudio.loan.service.LoanService;
import com.bookstudio.shared.util.ApiError;
import com.bookstudio.shared.util.ApiResponse;
import com.bookstudio.shared.util.SelectOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

	private final LoanService loanService;

	@GetMapping
	public ResponseEntity<?> list() {
		List<LoanListProjection> loans = loanService.getList();
		if (loans.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(new ApiError(false, "No loans found.", "no_content", 204));
		}
		return ResponseEntity.ok(loans);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> get(@PathVariable Long id) {
		LoanInfoProjection loan = loanService.getInfoById(id).orElse(null);
		if (loan == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiError(false, "Loan not found.", "not_found", 404));
		}
		return ResponseEntity.ok(loan);
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody CreateLoanDto dto) {
		try {
			LoanResponseDto created = loanService.create(dto);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, created));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ApiError(false, e.getMessage(), "creation_failed", 400));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@RequestBody UpdateLoanDto dto) {
		try {
			LoanResponseDto result = loanService.update(dto);
			return ResponseEntity.ok(new ApiResponse(true, result));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiError(false, e.getMessage(), "update_failed", 404));
		}
	}

	@PatchMapping("/{id}/return")
	public ResponseEntity<?> markAsReturned(@PathVariable Long id) {
		try {
			int result = loanService.markAsReturned(id);
			if (result == 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ApiError(false, "Loan already returned or not found.", "invalid_operation", 400));
			}
			return ResponseEntity.ok(new ApiResponse(true, id));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiError(false, "Server error while returning loan status.", "server_error", 500));
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
