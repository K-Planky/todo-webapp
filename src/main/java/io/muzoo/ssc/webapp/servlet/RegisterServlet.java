package io.muzoo.ssc.webapp.servlet;

import io.muzoo.ssc.webapp.Routable;
import io.muzoo.ssc.webapp.repository.DuplicateUsernameException;
import io.muzoo.ssc.webapp.service.UserService;
import io.muzoo.ssc.webapp.service.UserServiceAware;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

public class RegisterServlet extends HttpServlet implements Routable, UserServiceAware {

    private UserService userService;

    @Override
    public String getMapping() {
        return "/register";
    }

    @Override
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        if (!Objects.equals(password, confirmPassword)) {
            req.setAttribute("error", "Passwords do not match.");
            req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
            return;
        }

        try {
            userService.register(username, password);
        } catch (DuplicateUsernameException e) {
            req.setAttribute("error", "That username is already taken.");
            req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
            return;
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
            return;
        }

        req.getSession().setAttribute("flashMessage", "Account created — please log in.");
        resp.sendRedirect(req.getContextPath() + "/login");
    }

}
