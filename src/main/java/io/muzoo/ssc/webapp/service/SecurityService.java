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
            HttpSession session = request.getSession();
            request.changeSessionId();
            session.setAttribute(SessionKeys.USER_ID, user.get().id());
            session.setAttribute(SessionKeys.USERNAME, user.get().username());
            return true;
        }
        return false;
    }

    public boolean isAuthorized(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(SessionKeys.USER_ID) != null;
    }

    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }

}
