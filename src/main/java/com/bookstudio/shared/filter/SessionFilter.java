package com.bookstudio.shared.filter;

import com.bookstudio.auth.util.LoginConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SessionFilter extends OncePerRequestFilter {

	private static final String LOGIN_PAGE = "/login";
	private static final String DASHBOARD_PAGE = "/";
	private static final String RESET_PASSWORD_PAGE = "/reset-password";
	private static final String FORGOT_PASSWORD_PAGE = "/forgot-password";

	private static final String LOGIN_API = "/api/auth/login";
	private static final String LOGOUT_API = "/api/auth/logout";
	private static final String RESET_PASSWORD_API = "/api/auth/reset-password";
	private static final String FORGOT_PASSWORD_API = "/api/auth/forgot-password";
	private static final String VALIDATE_TOKEN_API = "/api/auth/validate-token";

	private static final String[] PUBLIC_PATHS = {
			LOGIN_PAGE,
			RESET_PASSWORD_PAGE,
			FORGOT_PASSWORD_PAGE,
			LOGIN_API,
			LOGOUT_API,
			RESET_PASSWORD_API,
			FORGOT_PASSWORD_API,
			VALIDATE_TOKEN_API
	};

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain)
				throws ServletException, IOException {

		String contextPath = request.getContextPath();
		String requestURI = request.getRequestURI();
		String relativePath = requestURI.substring(contextPath.length());

		HttpSession session = request.getSession(false);
		boolean loggedIn = session != null && session.getAttribute("user") != null;

		if (loggedIn) {
			if (isIn(relativePath, LOGIN_PAGE, RESET_PASSWORD_PAGE, FORGOT_PASSWORD_PAGE)) {
				response.sendRedirect(contextPath + DASHBOARD_PAGE);
				return;
			}

			if (relativePath.equals("/users")) {
				if (session != null) {
					String userRole = (String) session.getAttribute(LoginConstants.ROLE);
					if (userRole == null || !userRole.equals("administrador")) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
						return;
					}
				}
			}

			filterChain.doFilter(request, response);
			return;
		}

		if (isPublic(relativePath) || isStatic(relativePath)) {
			filterChain.doFilter(request, response);
		} else {
			response.sendRedirect(contextPath + LOGIN_PAGE);
		}
	}

	private boolean isPublic(String path) {
		for (String allowed : PUBLIC_PATHS) {
			if (path.equals(allowed))
				return true;
		}
		return false;
	}

	private boolean isStatic(String path) {
		return path.matches(".*\\.(css|js|png|jpg|jpeg|gif|svg|ico|woff2?|ttf|eot|map)$");
	}

	private boolean isIn(String target, String... paths) {
		for (String p : paths) {
			if (target.equals(p))
				return true;
		}
		return false;
	}
}
