package com.bookstudio.shared.api;

import com.bookstudio.auth.util.LoginConstants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseApiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");
		super.service(request, response);
	}

	protected boolean isAuthorized(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userRole = (String) request.getSession().getAttribute(LoginConstants.ROLE);
		if (userRole == null || !userRole.equals("administrador")) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.getWriter().write(
					"{\"success\": false, \"message\": \"Unauthorized: insufficient role.\", \"statusCode\": 403}");
			return false;
		}
		return true;
	}

	protected void handleException(HttpServletResponse response, String message, Exception e) throws IOException {
		e.printStackTrace();
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.getWriter().write("{\"success\": false, \"message\": \"" + message
				+ "\", \"errorType\": \"server_error\", \"statusCode\": 500, \"details\": \"" + e.getMessage() + "\"}");
	}
}
