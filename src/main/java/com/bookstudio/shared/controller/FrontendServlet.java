package com.bookstudio.shared.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {
	"/login", "/forgot-password", "/reset-password",
	"", "/authors", "/books", "/courses",
	"/loans", "/profile", "/publishers", "/students", "/users"
})
public class FrontendServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String jspPath = getJspPath(path);

        if (jspPath == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.getRequestDispatcher(jspPath).forward(request, response);
    }

    private String getJspPath(String path) {
        return switch (path) {
            case "/login" -> "/WEB-INF/views/auth/login.jsp";
            case "/forgot-password" -> "/WEB-INF/views/auth/forgot-password.jsp";
            case "/reset-password" -> "/WEB-INF/views/auth/reset-password.jsp";
            case "" -> "/WEB-INF/views/dashboard/dashboard.jsp";
            case "/authors" -> "/WEB-INF/views/authors/authors.jsp";
            case "/books" -> "/WEB-INF/views/books/books.jsp";
            case "/courses" -> "/WEB-INF/views/courses/courses.jsp";
            case "/loans" -> "/WEB-INF/views/loans/loans.jsp";
            case "/profile" -> "/WEB-INF/views/profile/profile.jsp";
            case "/publishers" -> "/WEB-INF/views/publishers/publishers.jsp";
            case "/students" -> "/WEB-INF/views/students/students.jsp";
            case "/users" -> "/WEB-INF/views/users/users.jsp";
            default -> null;
        };
    }
}
