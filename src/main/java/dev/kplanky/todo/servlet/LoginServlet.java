package dev.kplanky.todo.servlet;

import dev.kplanky.todo.Routable;
import dev.kplanky.todo.service.SecurityService;
import dev.kplanky.todo.service.SecurityServiceAware;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class LoginServlet extends HttpServlet implements Routable, SecurityServiceAware {

    private SecurityService securityService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object flash = req.getSession().getAttribute("flashError");
        if (flash != null) {
            req.setAttribute("error", flash);
            req.getSession().removeAttribute("flashError");
        }
        Object message = req.getSession().getAttribute("flashMessage");
        if (message != null) {
            req.setAttribute("message", message);
            req.getSession().removeAttribute("flashMessage");
        }
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            req.getSession().setAttribute("flashError", "Username or password is missing.");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (securityService.authenticate(username, password, req)) {
            resp.sendRedirect(req.getContextPath() + "/todos");
        } else {
            req.getSession().setAttribute("flashError", "Invalid username or password");
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    @Override
    public String getMapping() {
        return "/login";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

}
