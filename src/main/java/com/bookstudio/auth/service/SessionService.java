package com.bookstudio.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    public void saveSessionString(HttpServletRequest request, String key, String value) {
        HttpSession session = request.getSession();
        session.setAttribute(key, value);
    }

    public void saveSessionTimeOut(HttpServletRequest request, int seconds) {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(seconds);
    }

    public void invalidateSession(HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
