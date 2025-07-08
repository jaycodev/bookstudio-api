package com.bookstudio.shared.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @GetMapping({"/login", "/forgot-password", "/reset-password",
                 "/", "/authors", "/books", "/courses",
                 "/loans", "/profile", "/publishers", "/students", "/users"})
    public String handleFrontendRoutes() {
        return switch (getCurrentPath()) {
            case "/login" -> "auth/login";
            case "/forgot-password" -> "auth/forgot-password";
            case "/reset-password" -> "auth/reset-password";
            case "/" -> "dashboard/dashboard";
            case "/authors" -> "authors/authors";
            case "/books" -> "books/books";
            case "/courses" -> "courses/courses";
            case "/loans" -> "loans/loans";
            case "/profile" -> "profile/profile";
            case "/publishers" -> "publishers/publishers";
            case "/students" -> "students/students";
            case "/users" -> "users/users";
            default -> "error/404";
        };
    }

    private String getCurrentPath() {
        var request = org.springframework.web.context.request.RequestContextHolder
                .currentRequestAttributes();
        var servletRequest = ((org.springframework.web.context.request.ServletRequestAttributes) request)
                .getRequest();
        return servletRequest.getServletPath();
    }
}
