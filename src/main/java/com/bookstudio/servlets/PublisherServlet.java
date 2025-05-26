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

import com.bookstudio.models.Publisher;
import com.bookstudio.services.PublisherService;
import com.bookstudio.utils.LocalDateAdapter;
import com.bookstudio.utils.LoginConstants;
import com.bookstudio.utils.SelectOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@MultipartConfig(maxFileSize = 1048576, maxRequestSize = 1048576, fileSizeThreshold = 0)

@WebServlet("/PublisherServlet")
public class PublisherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private PublisherService publisherService = new PublisherService();
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
			listPublishers(request, response);
			break;
		case "details":
			getPublisher(request, response);
			break;
		case "populateSelects":
			populateSelects(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Invalid action or GET method not allowed for this action");
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
			createPublisher(request, response);
			break;
		case "update":
			updatePublisher(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
			break;
		}
	}

	private void listPublishers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<Publisher> publisherData = publisherService.listPublishers();

			if (publisherData != null && !publisherData.isEmpty()) {
				String json = gson.toJson(publisherData);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write("{\"success\": false, \"message\": \"No publishers found.\", \"errorType\": \"no_content\", \"statusCode\": 204}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Error listing publishers.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void getPublisher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String publisherId = request.getParameter("publisherId");

			if (publisherId == null || publisherId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"success\": false, \"message\": \"Publisher ID is required.\", \"errorType\": \"invalid_request\", \"statusCode\": 400}");
				return;
			}

			Publisher publisher = publisherService.getPublisher(publisherId);

			if (publisher != null) {
				String json = gson.toJson(publisher);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("{\"success\": false, \"message\": \"Publisher not found.\", \"errorType\": \"not_found\", \"statusCode\": 404}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Error retrieving publisher data.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void createPublisher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Publisher createdPublisher = publisherService.createPublisher(request);

			if (createdPublisher != null) {
				byte[] photo = createdPublisher.getPhoto();
				if (photo != null) {
					createdPublisher.setPhotoBase64("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo));
				}
				String json = gson.toJson(createdPublisher);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"success\": false, \"message\": \"Creation failed: Publisher data is null.\", \"errorType\": \"creation_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Server error while creating publisher.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void updatePublisher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Publisher updatedPublisher = publisherService.updatePublisher(request);
			if (updatedPublisher != null) {
				byte[] photo = updatedPublisher.getPhoto();
				if (photo != null) {
					updatedPublisher.setPhotoBase64("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo));
				}
				updatedPublisher.setPhoto(null);
				String json = gson.toJson(updatedPublisher);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"success\": false, \"message\": \"Update failed: Publisher data is null.\", \"errorType\": \"update_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Server error while updating publisher.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void populateSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			SelectOptions selectOptions = publisherService.populateSelects();

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
