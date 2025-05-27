package com.bookstudio.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.models.Author;
import com.bookstudio.services.AuthorService;
import com.bookstudio.utils.LocalDateAdapter;
import com.bookstudio.utils.LoginConstants;
import com.bookstudio.utils.SelectOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@MultipartConfig(maxFileSize = 1048576, maxRequestSize = 1048576, fileSizeThreshold = 0)

@WebServlet("/AuthorServlet")
public class AuthorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AuthorService authorService = new AuthorService();
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
			listAuthors(request, response);
			break;
		case "details":
			getAuthor(request, response);
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
		
	    String userRole = (String) request.getSession().getAttribute(LoginConstants.ROLE);
	    if (userRole == null || !userRole.equals("administrador")) {
	        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	        response.getWriter().write("{\"success\": false, \"message\": \"Unauthorized: insufficient role.\", \"statusCode\": 403}");
	        return;
	    }

		String type = request.getParameter("type");
		switch (type) {
		case "create":
			createAuthor(request, response);
			break;
		case "update":
			updateAuthor(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
			break;
		}
	}

	private void listAuthors(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<Author> authorData = authorService.listAuthors();

			if (authorData != null && !authorData.isEmpty()) {
				String json = gson.toJson(authorData);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write("{\"success\": false, \"message\": \"No authors found.\", \"errorType\": \"no_content\", \"statusCode\": 204}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Error listing authors.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void getAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String authorId = request.getParameter("authorId");

			if (authorId == null || authorId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"success\": false, \"message\": \"Author ID is required.\", \"errorType\": \"invalid_request\", \"statusCode\": 400}");
				return;
			}

			Author author = authorService.getAuthor(authorId);

			if (author != null) {
				String json = gson.toJson(author);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("{\"success\": false, \"message\": \"Author not found.\", \"errorType\": \"not_found\", \"statusCode\": 404}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Error retrieving author data.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void createAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Author createdAuthor = authorService.createAuthor(request);

			if (createdAuthor != null) {
				byte[] photo = createdAuthor.getPhoto();
				if (photo != null) {
					createdAuthor.setPhotoBase64("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo));
				}
				String json = gson.toJson(createdAuthor);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"success\": false, \"message\": \"Creation failed: Author data is null.\", \"errorType\": \"creation_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Server error while creating author.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void updateAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Author updatedAuthor = authorService.updateAuthor(request);
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
				response.getWriter().write("{\"success\": false, \"message\": \"Update failed: Author data is null.\", \"errorType\": \"update_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Server error while updating author.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void populateSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			SelectOptions selectOptions = authorService.populateSelects();

			if (selectOptions != null && (selectOptions.getNationalities() != null || selectOptions.getLiteraryGenres() != null)) {
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
