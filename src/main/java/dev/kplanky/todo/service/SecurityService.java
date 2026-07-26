package dev.kplanky.todo.service;

import dev.kplanky.todo.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

public class SecurityService {

    private final UserService userService;
    private final CsrfService csrfService;

    public SecurityService(UserService userService, CsrfService csrfService) {
        this.userService = userService;
        this.csrfService = csrfService;
    }

    public boolean authenticate(String username, String password, HttpServletRequest request) {
        Optional<User> user = userService.authenticate(username, password);

        if (user.isPresent()) {
            HttpSession session = request.getSession();
            request.changeSessionId();
            // The session id is rotated to defeat fixation; rotate the CSRF token with it for the
            // same reason. A token minted before login should not carry over into the
            // authenticated session, since anyone who saw the pre-login one would still know it.
            csrfService.reset(request);
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
