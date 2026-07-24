package dev.kplanky.todo.servlet;

import dev.kplanky.todo.Routable;
import dev.kplanky.todo.service.SecurityService;
import dev.kplanky.todo.service.SecurityServiceAware;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LogoutServlet extends HttpServlet implements Routable, SecurityServiceAware {

    private SecurityService securityService;

    @Override
    public String getMapping() {
        return "/logout";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        securityService.logout(req);
        resp.sendRedirect(req.getContextPath() + "/login");
    }

}
