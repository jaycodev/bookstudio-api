package com.bookstudio.auth.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookstudio.auth.dao.PasswordResetTokenDao;
import com.bookstudio.auth.dao.PasswordResetTokenDaoImpl;
import com.google.gson.JsonObject;

@WebServlet("/ValidateTokenServlet")
public class ValidateTokenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		String token = request.getParameter("token");

		PasswordResetTokenDao tokenDao = new PasswordResetTokenDaoImpl();

		boolean isValid = tokenDao.isTokenValid(token);

		JsonObject jsonResponse = new JsonObject();
		jsonResponse.addProperty("valid", isValid);
		out.print(jsonResponse);
		out.flush();
	}
}
