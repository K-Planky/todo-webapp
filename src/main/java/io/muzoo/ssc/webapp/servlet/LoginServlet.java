package io.muzoo.ssc.webapp.servlet;

import io.muzoo.ssc.webapp.Routable;
import io.muzoo.ssc.webapp.service.SecurityService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class LoginServlet extends HttpServlet implements Routable {

    private SecurityService securityService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object flash = request.getSession().getAttribute("flashError");
        if (flash != null) {
            request.setAttribute("error", flash);
            request.getSession().removeAttribute("flashError");
        }
        Object message = request.getSession().getAttribute("flashMessage");
        if (message != null) {
            request.setAttribute("message", message);
            request.getSession().removeAttribute("flashMessage");
        }
        request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            request.getSession().setAttribute("flashError", "Username or password is missing.");
            response.sendRedirect("/login");
            return;
        }

        if (securityService.authenticate(username, password, request)) {
            response.sendRedirect("/");
        } else {
            request.getSession().setAttribute("flashError", "Invalid username or password");
            response.sendRedirect("/login");
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
