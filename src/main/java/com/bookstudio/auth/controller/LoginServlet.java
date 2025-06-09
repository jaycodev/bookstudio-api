package com.bookstudio.auth.controller;

import java.io.IOException;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookstudio.auth.dao.AuthDao;
import com.bookstudio.auth.dao.AuthDaoImpl;
import com.bookstudio.auth.dao.SessionDaoImpl;
import com.bookstudio.auth.util.LoginConstants;
import com.bookstudio.user.model.User;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final int SESSION_TIMEOUT_MINUTES = 15;
	private static final int MAX_ATTEMPTS = 5;
	private static final int BLOCK_DURATION_MINUTES = 3;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");

		if (type.equals("login")) {
			Cookie blockTimeCookie = null;
			Cookie attemptsCookie = null;
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("blockTime".equals(cookie.getName())) {
						blockTimeCookie = cookie;
					}
					if ("loginAttempts".equals(cookie.getName())) {
						attemptsCookie = cookie;
					}
				}
			}

			if (blockTimeCookie != null) {
				try {
					long blockTimeValue = Long.parseLong(blockTimeCookie.getValue());
					if (System.currentTimeMillis() < blockTimeValue) {
						long remainingSeconds = (blockTimeValue - System.currentTimeMillis()) / 1000;
						response.setContentType("application/json");
						response.getWriter().write(
								"{\"success\": false, \"message\": \"Demasiados intentos fallidos. Intenta nuevamente en "
										+ remainingSeconds + " segundos.\"}");
						return;
					} else {
						blockTimeCookie.setMaxAge(0);
						response.addCookie(blockTimeCookie);
						if (attemptsCookie != null) {
							attemptsCookie.setMaxAge(0);
							response.addCookie(attemptsCookie);
						}
					}
				} catch (NumberFormatException e) {
					blockTimeCookie.setMaxAge(0);
					response.addCookie(blockTimeCookie);
				}
			}

			String username = request.getParameter("txtUsername");
			String password = request.getParameter("txtPassword");

			AuthDao authDao = new AuthDaoImpl();
			User user = authDao.verifyLogin(username, password);

			if (user != null) {
				if (blockTimeCookie != null) {
					blockTimeCookie.setMaxAge(0);
					response.addCookie(blockTimeCookie);
				}
				if (attemptsCookie != null) {
					attemptsCookie.setMaxAge(0);
					response.addCookie(attemptsCookie);
				}

				HttpSession session = request.getSession();
				SessionDaoImpl sessionProject = new SessionDaoImpl();
				sessionProject.saveSessionTimeOut(request, SESSION_TIMEOUT_MINUTES * 60);
				sessionProject.saveSessionString(request, LoginConstants.ID, String.valueOf(user.getUserId()));
				sessionProject.saveSessionString(request, LoginConstants.USERNAME, user.getUsername());
				sessionProject.saveSessionString(request, LoginConstants.FIRSTNAME, user.getFirstName());
				sessionProject.saveSessionString(request, LoginConstants.LASTNAME, user.getLastName());
				sessionProject.saveSessionString(request, LoginConstants.EMAIL, user.getEmail());
				sessionProject.saveSessionString(request, LoginConstants.ROLE, user.getRole());

				byte[] photoBytes = user.getProfilePhoto();
				if (photoBytes != null) {
					String photoBase64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photoBytes);
					sessionProject.saveSessionString(request, LoginConstants.USER_PROFILE_IMAGE, photoBase64);
				}

				session.setAttribute("user", user);

				response.setContentType("application/json");
				response.getWriter().write("{\"success\": true, \"message\": \"Inicio de sesión exitoso.\"}");
			} else {
				int attempts = 0;
				if (attemptsCookie != null) {
					try {
						attempts = Integer.parseInt(attemptsCookie.getValue());
					} catch (NumberFormatException e) {
						attempts = 0;
					}
				}
				attempts++;

				Cookie newAttemptsCookie = new Cookie("loginAttempts", String.valueOf(attempts));
				newAttemptsCookie.setMaxAge(60 * 60);
				response.addCookie(newAttemptsCookie);

				if (attempts >= MAX_ATTEMPTS) {
					long lockUntil = System.currentTimeMillis() + (BLOCK_DURATION_MINUTES * 60 * 1000);
					Cookie newBlockCookie = new Cookie("blockTime", String.valueOf(lockUntil));
					newBlockCookie.setMaxAge(BLOCK_DURATION_MINUTES * 60);
					response.addCookie(newBlockCookie);

					response.setContentType("application/json");
					response.getWriter().write(
							"{\"success\": false, \"message\": \"Demasiados intentos fallidos. Estás bloqueado por "
									+ BLOCK_DURATION_MINUTES + " minutos.\"}");
				} else {
					response.setContentType("application/json");
					response.getWriter().write("{\"success\": false, \"message\": \"Usuario y/o contraseña incorrectos.\"}");
				}
			}
		} else if (type.equals("logout")) {
			SessionDaoImpl sessionProject = new SessionDaoImpl();
			sessionProject.invalidateSession(request);
			response.sendRedirect(request.getContextPath() + "/login");
		}
	}
}
