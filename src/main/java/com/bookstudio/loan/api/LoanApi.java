package com.bookstudio.loan.api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.loan.model.Loan;
import com.bookstudio.loan.service.LoanService;
import com.bookstudio.shared.api.BaseApiServlet;
import com.bookstudio.shared.util.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@MultipartConfig
@WebServlet(name = "LoanApi", urlPatterns = { "/api/loans", "/api/loans/*" })
public class LoanApi extends BaseApiServlet {
	private static final long serialVersionUID = 1L;

	private LoanService loanService = new LoanService();
	private Gson gson;

	@Override
	public void init() throws ServletException {
		gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			listLoans(response);
		} else if (pathInfo.startsWith("/")) {
			String loanId = pathInfo.substring(1);
			getLoan(loanId, response);
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid GET request");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String overrideMethod = request.getParameter("_method");

		if ("PUT".equalsIgnoreCase(overrideMethod) || "PATCH".equalsIgnoreCase(overrideMethod)) {
			String pathInfo = request.getPathInfo();
			if (pathInfo != null && pathInfo.startsWith("/")) {
				String loanId = pathInfo.substring(1);

				if ("PUT".equalsIgnoreCase(overrideMethod)) {
					updateLoan(loanId, request, response);
				} else {
					confirmReturn(loanId, request, response);
				}
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Loan ID is required for this operation.");
			}
		} else {
			createLoan(request, response);
		}
	}

	private void listLoans(HttpServletResponse response) throws IOException {
		try {
			List<Loan> loans = loanService.listLoans();
			if (loans != null && !loans.isEmpty()) {
				String json = gson.toJson(loans);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"No loans found.\", \"errorType\": \"no_content\", \"statusCode\": 204}");
			}
		} catch (Exception e) {
			handleException(response, "Error listing loans.", e);
		}
	}

	private void getLoan(String loanId, HttpServletResponse response) throws IOException {
		try {
			if (loanId == null || loanId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Loan ID is required.\", \"errorType\": \"invalid_request\", \"statusCode\": 400}");
				return;
			}

			Loan loan = loanService.getLoan(loanId);
			if (loan != null) {
				String json = gson.toJson(loan);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Loan not found.\", \"errorType\": \"not_found\", \"statusCode\": 404}");
			}
		} catch (Exception e) {
			handleException(response, "Error retrieving loan data.", e);
		}
	}

	private void createLoan(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			Loan createdLoan = loanService.createLoan(request);
			if (createdLoan != null) {
				String json = gson.toJson(createdLoan);
				response.setStatus(HttpServletResponse.SC_CREATED);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Creation failed: Loan data is null.\", \"errorType\": \"creation_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			handleException(response, "Server error while creating loan.", e);
		}
	}

	private void updateLoan(String loanId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			if (loanId == null || loanId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter()
						.write("{\"success\": false, \"message\": \"Loan ID is required.\", \"statusCode\": 400}");
				return;
			}

			Loan updatedLoan = loanService.updateLoan(loanId, request);
			if (updatedLoan != null) {
				String json = gson.toJson(updatedLoan);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Update failed: Loan data is null.\", \"errorType\": \"update_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			handleException(response, "Server error while updating loan.", e);
		}
	}

	private void confirmReturn(String loanId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			if (loanId == null || loanId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter()
						.write("{\"success\": false, \"message\": \"Loan ID is required.\", \"statusCode\": 400}");
				return;
			}

			Loan loan = loanService.getLoan(loanId);
			String currentStatus = loan.getStatus();
			if ("devuelto".equals(currentStatus)) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"The loan has already been returned.\", \"errorType\": \"already_returned\", \"statusCode\": 400}");
				return;
			}

			int result = loanService.confirmReturn(loanId);
			if (result > 0) {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"loanId\": \"" + loanId + "\"}");
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Error returning loan status.\", \"errorType\": \"update_failed\", \"statusCode\": 500}");
			}
		} catch (Exception e) {
			handleException(response, "Server error while returning loan status.", e);
		}
	}
}
