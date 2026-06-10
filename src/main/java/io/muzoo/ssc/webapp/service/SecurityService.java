package io.muzoo.ssc.webapp.service;

import io.muzoo.ssc.webapp.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

public class SecurityService {

    private final UserService userService;

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public boolean authenticate(String username, String password, HttpServletRequest request) {
        Optional<User> user = userService.authenticate(username, password);

        if (user.isPresent()) {
            request.getSession();
            request.changeSessionId();
            request.getSession().setAttribute("userId", user.get().id());
            request.getSession().setAttribute("username", user.get().username());
            return true;
        }
        return false;
    }

    public boolean isAuthorized(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("userId") != null;
    }

    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }

}
