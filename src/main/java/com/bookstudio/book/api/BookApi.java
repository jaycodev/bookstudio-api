package com.bookstudio.book.api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.book.model.Book;
import com.bookstudio.book.service.BookService;
import com.bookstudio.shared.api.BaseApiServlet;
import com.bookstudio.shared.util.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet(name = "BookApi", urlPatterns = { "/api/books", "/api/books/*" })
public class BookApi extends BaseApiServlet {
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
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			listBooks(request, response);
		} else if (pathInfo.startsWith("/")) {
			String bookId = pathInfo.substring(1);
			getBook(bookId, response);
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid GET request");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    String overrideMethod = request.getParameter("_method");

	    if ("PUT".equalsIgnoreCase(overrideMethod)) {
	        handleUpdate(request, response);
	    } else {
	        handleCreate(request, response);
	    }
	}

	private void listBooks(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			List<Book> bookData = bookService.listBooks();

			if (bookData != null && !bookData.isEmpty()) {
				String json = gson.toJson(bookData);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"No books found.\", \"errorType\": \"no_content\", \"statusCode\": 204}");
			}
		} catch (Exception e) {
			handleException(response, "Error listing books.", e);
		}
	}

	private void getBook(String bookId, HttpServletResponse response) throws IOException {
		try {
			if (bookId == null || bookId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Book ID is required.\", \"errorType\": \"invalid_request\", \"statusCode\": 400}");
				return;
			}

			Book book = bookService.getBook(bookId);

			if (book != null) {
				String json = gson.toJson(book);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Book not found.\", \"errorType\": \"not_found\", \"statusCode\": 404}");
			}
		} catch (Exception e) {
			handleException(response, "Error retrieving book data.", e);
		}
	}

	private void handleCreate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!isAuthorized(request, response))
			return;
		try {
			Book createdBook = bookService.createBook(request);
			if (createdBook != null) {
				String json = gson.toJson(createdBook);
				response.setStatus(HttpServletResponse.SC_CREATED);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Creation failed: Book data is null.\", \"errorType\": \"creation_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			handleException(response, "Server error while creating book.", e);
		}
	}

	private void handleUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!isAuthorized(request, response))
			return;
		try {
			Book updatedBook = bookService.updateBook(request);
			if (updatedBook != null) {
				String json = gson.toJson(updatedBook);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Update failed: Book data is null.\", \"errorType\": \"update_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			handleException(response, "Server error while updating book.", e);
		}
	}
}
