package com.bookstudio.book.api;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.book.service.BookService;
import com.bookstudio.shared.util.LocalDateAdapter;
import com.bookstudio.shared.util.SelectOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet(name = "BookOptionsApi", urlPatterns = { "/api/books/select-options" })
public class BookOptionsApi extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BookService bookService = new BookService();
	private Gson gson;

	@Override
	public void init() throws ServletException {
		gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");

		try {
			SelectOptions options = bookService.populateSelects();

			if (options != null && (options.getAuthors() != null || options.getPublishers() != null
					|| options.getCourses() != null || options.getGenres() != null)) {
				String json = gson.toJson(options);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"No select options found.\", \"errorType\": \"no_content\", \"statusCode\": 204}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(
					"{\"success\": false, \"message\": \"Error populating select options.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \""
							+ e.getMessage() + "\"}");
		}
	}
}
