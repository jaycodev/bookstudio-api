package com.bookstudio.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.models.User;
import com.bookstudio.services.UserService;
import com.bookstudio.utils.LocalDateAdapter;
import com.bookstudio.utils.LoginConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@MultipartConfig(maxFileSize = 1048576, maxRequestSize = 1048576, fileSizeThreshold = 0)

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserService userService = new UserService();
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
		
	    String userRole = (String) request.getSession().getAttribute(LoginConstants.ROLE);
	    if (userRole == null || !userRole.equals("administrador")) {
	        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	        response.getWriter().write("{\"success\": false, \"message\": \"Unauthorized: insufficient role.\", \"statusCode\": 403}");
	        return;
	    }
		
		String type = request.getParameter("type");
		switch (type) {
		case "list":
			listUsers(request, response);
			break;
		case "details":
			getUser(request, response);
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
			createUser(request, response);
			break;
		case "update":
			updateUser(request, response);
			break;
		case "delete":
			deleteUser(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
			break;
		}
	}

	private void listUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<User> userData = userService.listUsers(request);

			if (userData != null && !userData.isEmpty()) {
				String json = gson.toJson(userData);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write("{\"success\": false, \"message\": \"No users found.\", \"errorType\": \"no_content\", \"statusCode\": 204}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Error listing users.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void getUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String userId = request.getParameter("userId");

			if (userId == null || userId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"success\": false, \"message\": \"User ID is required.\", \"errorType\": \"invalid_request\", \"statusCode\": 400}");
				return;
			}

			User user = userService.getUser(userId);

			if (user != null) {
				String json = gson.toJson(user);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(json);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("{\"success\": false, \"message\": \"User not found.\", \"errorType\": \"not_found\", \"statusCode\": 404}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Error retrieving user data.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void createUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
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
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Server error while creating user.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			User updatedUser = userService.updateUser(request);
			if (updatedUser != null) {
				byte[] profilePhoto = updatedUser.getProfilePhoto();
				if (profilePhoto != null) {
					updatedUser.setProfilePhotoBase64("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(profilePhoto));
				}
				updatedUser.setProfilePhoto(null);
				String json = gson.toJson(updatedUser);
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true, \"data\": " + json + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"success\": false, \"message\": \"Update failed: User data is null.\", \"errorType\": \"update_failed\", \"statusCode\": 400}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"message\": \"Server error while updating user.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
		}
	}

	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String userId = request.getParameter("userId");

			if (userId == null || userId.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"success\": false, \"message\": \"User ID is required.\", \"errorType\": \"invalid_request\", \"statusCode\": 400}");
				return;
			}
			
			boolean deletedUser = userService.deleteUser(userId);
			
			if (deletedUser) {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"success\": true}");
			} else {
	            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	            response.getWriter().write("{\"success\": false, \"message\": \"There was an error deleting the user.\", \"errorType\": \"deletion_failed\", \"statusCode\": 404}");
			}
		} catch (Exception e) {
			e.printStackTrace();
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("{\"success\": false, \"message\": \"Server error while deleting user.\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
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
