package com.bookstudio.servlets;

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
            case "" -> "/WEB-INF/views/user/dashboard.jsp";
            case "/authors" -> "/WEB-INF/views/user/authors.jsp";
            case "/books" -> "/WEB-INF/views/user/books.jsp";
            case "/courses" -> "/WEB-INF/views/user/courses.jsp";
            case "/loans" -> "/WEB-INF/views/user/loans.jsp";
            case "/profile" -> "/WEB-INF/views/user/profile.jsp";
            case "/publishers" -> "/WEB-INF/views/user/publishers.jsp";
            case "/students" -> "/WEB-INF/views/user/students.jsp";
            case "/users" -> "/WEB-INF/views/user/users.jsp";
            default -> null;
        };
    }
}
