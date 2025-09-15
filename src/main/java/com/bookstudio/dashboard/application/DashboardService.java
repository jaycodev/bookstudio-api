package com.bookstudio.dashboard.application;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bookstudio.dashboard.domain.model.DashboardData;
import com.bookstudio.dashboard.domain.model.MonthlyLoanComparison;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

	private final JdbcTemplate jdbcTemplate;

	public DashboardData getDashboardData(int year1, int year2) {
		return DashboardData.builder()
				.totalActiveBooks(getCount("SELECT COUNT(*) FROM Books WHERE Status = 'activo'"))
				.totalActiveAuthors(getCount("SELECT COUNT(*) FROM Authors WHERE Status = 'activo'"))
				.totalActivePublishers(getCount("SELECT COUNT(*) FROM Publishers WHERE Status = 'activo'"))
				.totalActiveCourses(getCount("SELECT COUNT(*) FROM Courses WHERE Status = 'activo'"))
				.totalActiveStudents(getCount("SELECT COUNT(*) FROM Students WHERE Status = 'activo'"))
				.totalActiveLoans(getCount("SELECT COUNT(*) FROM Loans WHERE Status = 'prestado'"))
				.loansByMonth(getMonthlyData("""
						    SELECT MONTH(LoanDate) AS Month, COUNT(*) AS Total
						    FROM Loans
						    WHERE YEAR(LoanDate) = YEAR(CURDATE())
						    GROUP BY MONTH(LoanDate)
						"""))
				.returnsByMonth(getMonthlyData("""
						    SELECT MONTH(ReturnDate) AS Month, COUNT(*) AS Total
						    FROM Loans
						    WHERE YEAR(ReturnDate) = YEAR(CURDATE())
						    AND ReturnDate IS NOT NULL
						    AND Status = 'devuelto'
						    GROUP BY MONTH(ReturnDate)
						"""))
				.averageLoanDurationByMonth(getAverageLoanDurationByMonth())
				.monthlyLoanComparison(getMonthlyLoanComparison(year1, year2))
				.build();
	}

	private int getCount(String sql) {
		try {
			Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
			return (result != null) ? result : 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private Map<Integer, Integer> getMonthlyData(String sql) {
		Map<Integer, Integer> data = new HashMap<>();
		try {
			jdbcTemplate.query(sql, rs -> {
				data.put(rs.getInt("Month"), rs.getInt("Total"));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	private Map<Integer, Double> getAverageLoanDurationByMonth() {
		String sql = """
				    SELECT MONTH(LoanDate) AS Month, AVG(DATEDIFF(ReturnDate, LoanDate)) AS AvgDuration
				    FROM Loans
				    WHERE YEAR(LoanDate) = YEAR(CURDATE()) AND ReturnDate IS NOT NULL
				    GROUP BY MONTH(LoanDate)
				""";

		Map<Integer, Double> data = new HashMap<>();
		try {
			jdbcTemplate.query(sql, rs -> {
				data.put(rs.getInt("Month"), rs.getDouble("AvgDuration"));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	private List<MonthlyLoanComparison> getMonthlyLoanComparison(int year1, int year2) {
		String sql = """
				    SELECT MONTH(LoanDate) AS Month,
				           SUM(CASE WHEN YEAR(LoanDate) = ? THEN 1 ELSE 0 END) AS LoansYear1,
				           SUM(CASE WHEN YEAR(LoanDate) = ? THEN 1 ELSE 0 END) AS LoansYear2
				    FROM Loans
				    WHERE YEAR(LoanDate) IN (?, ?)
				    GROUP BY MONTH(LoanDate)
				    ORDER BY Month
				""";

		try {
			return jdbcTemplate.query(sql, ps -> {
				ps.setInt(1, year1);
				ps.setInt(2, year2);
				ps.setInt(3, year1);
				ps.setInt(4, year2);
			}, (rs, rowNum) -> MonthlyLoanComparison.builder()
					.month(rs.getInt("Month"))
					.loansYear1(rs.getInt("LoansYear1"))
					.loansYear2(rs.getInt("LoansYear2"))
					.build());
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
}
