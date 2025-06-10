package com.bookstudio.publisher.api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.publisher.model.Publisher;
import com.bookstudio.publisher.service.PublisherService;
import com.bookstudio.shared.api.BaseApiServlet;
import com.bookstudio.shared.util.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@MultipartConfig(maxFileSize = 1048576, maxRequestSize = 1048576, fileSizeThreshold = 0)
@WebServlet(name = "PublisherApi", urlPatterns = { "/api/publishers", "/api/publishers/*" })
public class PublisherApi extends BaseApiServlet {
	private static final long serialVersionUID = 1L;

	private PublisherService publisherService = new PublisherService();
	private Gson gson;

	@Override
	public void init() throws ServletException {
		gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			listPublishers(response);
		} else if (pathInfo.startsWith("/")) {
			String publisherId = pathInfo.substring(1);
			getPublisher(publisherId, response);
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid GET request.");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String overrideMethod = request.getParameter("_method");
		if ("PUT".equalsIgnoreCase(overrideMethod)) {
			String pathInfo = request.getPathInfo();
			if (pathInfo != null && pathInfo.startsWith("/")) {
				String publisherId = pathInfo.substring(1);
				updatePublisher(publisherId, request, response);
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Publisher ID is required for update.");
			}
		} else {
			createPublisher(request, response);
		}
	}

	private void listPublishers(HttpServletResponse response) throws IOException {
		try {
			List<Publisher> publishers = publisherService.listPublishers();
			if (publishers != null && !publishers.isEmpty()) {
				String json = gson.toJson(publishers);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"No publishers found.\", \"errorType\": \"no_content\", \"statusCode\": 204}");
			}
		} catch (Exception e) {
			handleException(response, "Error listing publishers.", e);
		}
	}

	private void getPublisher(String publisherId, HttpServletResponse response) throws IOException {
		try {
			if (publisherId == null || publisherId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Publisher ID is required.\", \"errorType\": \"invalid_request\", \"statusCode\": 400}");
				return;
			}

			Publisher publisher = publisherService.getPublisher(publisherId);
			if (publisher != null) {
				String json = gson.toJson(publisher);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Publisher not found.\", \"errorType\": \"not_found\", \"statusCode\": 404}");
			}
		} catch (Exception e) {
			handleException(response, "Error retrieving publisher.", e);
		}
	}

	private void createPublisher(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!isAuthorized(request, response))
			return;

		try {
			Publisher created = publisherService.createPublisher(request);
			if (created != null) {
				byte[] photo = created.getPhoto();
				if (photo != null) {
					created.setPhotoBase64("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo));
				}
				String json = gson.toJson(created);
				response.setStatus(HttpServletResponse.SC_CREATED);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Creation failed: Publisher data is null.\", \"errorType\": \"creation_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			handleException(response, "Server error while creating publisher.", e);
		}
	}

	private void updatePublisher(String publisherId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (!isAuthorized(request, response))
			return;

		try {
			if (publisherId == null || publisherId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter()
						.write("{\"success\": false, \"message\": \"Publisher ID is required.\", \"statusCode\": 400}");
				return;
			}

			Publisher updated = publisherService.updatePublisher(publisherId, request);
			if (updated != null) {
				byte[] photo = updated.getPhoto();
				if (photo != null) {
					updated.setPhotoBase64("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo));
				}
				updated.setPhoto(null);
				String json = gson.toJson(updated);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Update failed: Publisher data is null.\", \"errorType\": \"update_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			handleException(response, "Server error while updating publisher.", e);
		}
	}
}
