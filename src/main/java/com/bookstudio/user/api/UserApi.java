package com.bookstudio.user.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.shared.api.BaseApiServlet;
import com.bookstudio.shared.util.LocalDateAdapter;
import com.bookstudio.user.model.User;
import com.bookstudio.user.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@MultipartConfig(maxFileSize = 1048576, maxRequestSize = 1048576, fileSizeThreshold = 0)
@WebServlet(name = "UserApi", urlPatterns = { "/api/users", "/api/users/*" })
public class UserApi extends BaseApiServlet {
	private static final long serialVersionUID = 1L;

	private UserService userService = new UserService();
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
			listUsers(request, response);
		} else if (pathInfo.startsWith("/")) {
			String userId = pathInfo.substring(1);
			getUser(userId, response);
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid GET request.");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String overrideMethod = request.getParameter("_method");
		if ("PUT".equalsIgnoreCase(overrideMethod)) {
	        String pathInfo = request.getPathInfo();
	        if (pathInfo != null && pathInfo.startsWith("/")) {
	            String userId = pathInfo.substring(1);
	            updateUser(userId, request, response);
	        } else {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required for update.");
	        }
		} else {
			createUser(request, response);
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo != null && pathInfo.startsWith("/")) {
			String userId = pathInfo.substring(1);
			deleteUser(userId, request, response);
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required for deletion.");
		}
	}

	private void listUsers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			List<User> users = userService.listUsers(request);

			if (users != null && !users.isEmpty()) {
				String json = gson.toJson(users);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"No users found.\", \"errorType\": \"no_content\", \"statusCode\": 204}");
			}
		} catch (Exception e) {
			handleException(response, "Error listing users.", e);
		}
	}

	private void getUser(String userId, HttpServletResponse response) throws ServletException, IOException {
		try {
			if (userId == null || userId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"User ID is required.\", \"errorType\": \"invalid_request\", \"statusCode\": 400}");
				return;
			}

			User user = userService.getUser(userId);

			if (user != null) {
				String json = gson.toJson(user);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"User not found.\", \"errorType\": \"not_found\", \"statusCode\": 404}");
			}
		} catch (Exception e) {
			handleException(response, "Error retrieving user data.", e);
		}
	}

	private void createUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAuthorized(request, response))
			return;

		try {
			User createdUser = userService.createUser(request);

			if (createdUser != null) {
				String json = gson.toJson(createdUser);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Creation failed: User data is null.\", \"errorType\": \"creation_failed\", \"statusCode\": 400}");
			}
		} catch (SQLException e) {
			String errorMessage = e.getMessage();
			if (errorMessage.contains("nombre de usuario")) {
				sendErrorResponse(response, errorMessage, "addUserUsername");
			} else if (errorMessage.contains("correo electrónico")) {
				sendErrorResponse(response, errorMessage, "addUserEmail");
			} else {
				sendErrorResponse(response, "Error al crear el usuario.", "");
			}
		} catch (Exception e) {
			handleException(response, "Server error while creating user.", e);
		}
	}

	private void updateUser(String userId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAuthorized(request, response))
			return;

		try {
	        if (userId == null || userId.trim().isEmpty()) {
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            response.getWriter().write("{\"success\": false, \"message\": \"User ID is required.\", \"statusCode\": 400}");
	            return;
	        }
			
			User updatedUser = userService.updateUser(userId, request);
			if (updatedUser != null) {
				byte[] profilePhoto = updatedUser.getProfilePhoto();
				if (profilePhoto != null) {
					updatedUser.setProfilePhotoBase64(
							"data:image/jpeg;base64," + Base64.getEncoder().encodeToString(profilePhoto));
				}
				updatedUser.setProfilePhoto(null);
				String json = gson.toJson(updatedUser);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"Update failed: User data is null.\", \"errorType\": \"update_failed\", \"statusCode\": 400}");
			}
		} catch (SQLException e) {
			String errorMessage = e.getMessage();
			if (errorMessage.contains("nombre de usuario")) {
				sendErrorResponse(response, errorMessage, "editUserUsername");
			} else if (errorMessage.contains("correo electrónico")) {
				sendErrorResponse(response, errorMessage, "editUserEmail");
			} else {
				sendErrorResponse(response, "Error al actualizar el usuario.", "");
			}
		} catch (Exception e) {
			handleException(response, "Server error while updating user.", e);
		}
	}

	private void deleteUser(String userId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isAuthorized(request, response))
			return;
		
		try {
			if (userId == null || userId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"User ID is required.\", \"errorType\": \"invalid_request\", \"statusCode\": 400}");
				return;
			}

			boolean deletedUser = userService.deleteUser(userId);

			if (deletedUser) {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true}");
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write(
						"{\"success\": false, \"message\": \"There was an error deleting the user.\", \"errorType\": \"deletion_failed\", \"statusCode\": 404}");
			}
		} catch (Exception e) {
			handleException(response, "Server error while deleting user.", e);
		}
	}

	private void sendErrorResponse(HttpServletResponse response, String message, String field) throws IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		if (field == null || field.isEmpty()) {
			out.write("{\"success\": false, \"message\": \"" + message + "\"}");
		} else {
			out.write("{\"success\": false, \"message\": \"" + message + "\", \"field\": \"" + field + "\"}");
		}
		out.flush();
	}
}