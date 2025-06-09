package com.bookstudio.loan.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.loan.model.Loan;
import com.bookstudio.loan.service.LoanService;
import com.bookstudio.shared.util.LocalDateAdapter;
import com.bookstudio.shared.util.SelectOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/LoanServlet")
public class LoanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private LoanService loanService = new LoanService();
	private Gson gson;

	@Override
	public void init() throws ServletException {
		gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String type = request.getParameter("type");
		switch (type) {
		case "list":
			listLoans(request, response);
			break;
		case "details":
			getLoan(request, response);
			break;
		case "populateSelects":
			populateSelects(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action or GET method not allowed for this action");
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String type = request.getParameter("type");
		switch (type) {
		case "create":
			createLoan(request, response);
			break;
		case "update":
			updateLoan(request, response);
			break;
		case "confirmReturn":
			confirmReturn(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
			break;
		}
	}

	private void listLoans(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<Loan> loanData = loanService.listLoans();

			if (loanData != null && !loanData.isEmpty()) {
				String json = gson.toJson(loanData);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write("{\"success\": false, \"message\": \"No loans found.\", \"errorType\": \"no_content\", \"statusCode\": 204}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Error listing loans.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void getLoan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String loanId = request.getParameter("loanId");

			if (loanId == null || loanId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"success\": false, \"message\": \"Loan ID is required.\", \"errorType\": \"invalid_request\", \"statusCode\": 400}");
				return;
			}

			Loan loan = loanService.getLoan(loanId);

			if (loan != null) {
				String json = gson.toJson(loan);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("{\"success\": false, \"message\": \"Loan not found.\", \"errorType\": \"not_found\", \"statusCode\": 404}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Error retrieving loan data.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void createLoan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Loan createdLoan = loanService.createLoan(request);

			if (createdLoan != null) {
				String json = gson.toJson(createdLoan);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"success\": false, \"message\": \"Creation failed: Loan data is null.\", \"errorType\": \"creation_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Server error while creating loan.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void updateLoan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Loan updatedLoan = loanService.updateLoan(request);
			if (updatedLoan != null) {
				String json = gson.toJson(updatedLoan);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"success\": false, \"message\": \"Update failed: Loan data is null.\", \"errorType\": \"update_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Server error while updating loan.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void confirmReturn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String loanId = request.getParameter("loanId");

			Loan loan = loanService.getLoan(loanId);
			String currentStatus = loan.getStatus();
			if ("devuelto".equals(currentStatus)) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"success\": false, \"message\": \"The loan has already been returned.\", \"errorType\": \"already_returned\", \"statusCode\": 400}");
				return;
			}

			int result = loanService.confirmReturn(loanId);
			if (result > 0) {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"loanId\": \"" + loanId + "\"}");
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("{\"success\": false, \"message\": \"Error returning loan status.\", \"errorType\": \"update_failed\", \"statusCode\": 500}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Server error while returning loan status.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void populateSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			SelectOptions selectOptions = loanService.populateSelects();

			if (selectOptions != null && (selectOptions.getBooks() != null || selectOptions.getStudents() != null)) {
				String json = gson.toJson(selectOptions);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write("{\"success\": false, \"message\": \"No select options found.\", \"errorType\": \"no_content\", \"statusCode\": 204}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Error populating select options.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}
}
