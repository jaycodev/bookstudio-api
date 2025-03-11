package com.bookstudio.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.dao.PasswordResetTokenDao;
import com.bookstudio.dao.impl.PasswordResetTokenDaoImpl;

@WebServlet("/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		response.setContentType("application/json");

		String token = request.getParameter("token");
		String newPassword = request.getParameter("newPassword");

		PasswordResetTokenDao tokenDao = new PasswordResetTokenDaoImpl();
		String jsonResponse;

		if (tokenDao.isTokenValid(token)) {
			boolean updated = tokenDao.updatePassword(token, newPassword);
			if (updated) {
				jsonResponse = "{\"success\": true, \"message\": \"La contraseña se ha actualizado exitosamente.\"}";
			} else {
				jsonResponse = "{\"success\": false, \"message\": \"Ocurrió un error al actualizar la contraseña.\"}";
			}
		} else {
			jsonResponse = "{\"success\": false, \"message\": \"El enlace de restablecimiento ha expirado o es inválido.\"}";
		}

		response.getWriter().write(jsonResponse);
	}
}
