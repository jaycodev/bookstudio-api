package com.bookstudio.dashboard.domain.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyLoanComparison {
	private int month;
	private int loansYear1;
	private int loansYear2;
}
