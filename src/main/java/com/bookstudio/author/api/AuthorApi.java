package com.bookstudio.author.api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.author.model.Author;
import com.bookstudio.author.service.AuthorService;
import com.bookstudio.shared.api.BaseApiServlet;
import com.bookstudio.shared.util.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@MultipartConfig(maxFileSize = 1048576, maxRequestSize = 1048576, fileSizeThreshold = 0)
@WebServlet(name = "AuthorApi", urlPatterns = { "/api/authors", "/api/authors/*" })
public class AuthorApi extends BaseApiServlet {
	private static final long serialVersionUID = 1L;

	private AuthorService authorService = new AuthorService();
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
			listAuthors(response);
		} else if (pathInfo.startsWith("/")) {
			String authorId = pathInfo.substring(1);
			getAuthor(authorId, response);
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid GET request");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String overrideMethod = request.getParameter("_method");

		if ("PUT".equalsIgnoreCase(overrideMethod)) {
			String pathInfo = request.getPathInfo();
			if (pathInfo != null && pathInfo.startsWith("/")) {
				String authorId = pathInfo.substring(1);
				updateAuthor(authorId, request, response);
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Author ID is required for update.");
			}
		} else {
			createAuthor(request, response);
		}
	}

	private void listAuthors(HttpServletResponse response) throws IOException {
		try {
			List<Author> authors = authorService.listAuthors();
			if (authors != null && !authors.isEmpty()) {
				String json = gson.toJson(authors);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"No authors found.\", \"errorType\": \"no_content\", \"statusCode\": 204}");
			}
		} catch (Exception e) {
			handleException(response, "Error listing authors.", e);
		}
	}

	private void getAuthor(String authorId, HttpServletResponse response) throws IOException {
		try {
			if (authorId == null || authorId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Author ID is required.\", \"errorType\": \"invalid_request\", \"statusCode\": 400}");
				return;
			}

			Author author = authorService.getAuthor(authorId);
			if (author != null) {
				String json = gson.toJson(author);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Author not found.\", \"errorType\": \"not_found\", \"statusCode\": 404}");
			}
		} catch (Exception e) {
			handleException(response, "Error retrieving author data.", e);
		}
	}

	private void createAuthor(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!isAuthorized(request, response))
			return;

		try {
			Author createdAuthor = authorService.createAuthor(request);
			if (createdAuthor != null) {
				byte[] photo = createdAuthor.getPhoto();
				if (photo != null) {
					createdAuthor.setPhotoBase64("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo));
				}
				String json = gson.toJson(createdAuthor);
				response.setStatus(HttpServletResponse.SC_CREATED);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Creation failed: Author data is null.\", \"errorType\": \"creation_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			handleException(response, "Server error while creating author.", e);
		}
	}

	private void updateAuthor(String authorId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (!isAuthorized(request, response))
			return;

		try {
			if (authorId == null || authorId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter()
						.write("{\"success\": false, \"message\": \"Author ID is required.\", \"statusCode\": 400}");
				return;
			}

			Author updatedAuthor = authorService.updateAuthor(authorId, request);
			if (updatedAuthor != null) {
				byte[] photo = updatedAuthor.getPhoto();
				if (photo != null) {
					updatedAuthor.setPhotoBase64("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo));
				}
				updatedAuthor.setPhoto(null);
				String json = gson.toJson(updatedAuthor);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Update failed: Author data is null.\", \"errorType\": \"update_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			handleException(response, "Server error while updating author.", e);
		}
	}
}
