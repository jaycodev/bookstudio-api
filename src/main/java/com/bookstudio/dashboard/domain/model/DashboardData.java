package com.bookstudio.dashboard.domain.model;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardData {
	private int totalActiveBooks;
	private int totalActiveAuthors;
	private int totalActivePublishers;
	private int totalActiveCourses;
	private int totalActiveStudents;
	private int totalActiveLoans;

	private Map<Integer, Integer> loansByMonth;
	private Map<Integer, Integer> returnsByMonth;
	private Map<Integer, Double> averageLoanDurationByMonth;

	private List<MonthlyLoanComparison> monthlyLoanComparison;
}
