package io.muzoo.ssc.webapp.servlet;

import io.muzoo.ssc.webapp.Routable;
import io.muzoo.ssc.webapp.service.SecurityService;
import io.muzoo.ssc.webapp.service.SecurityServiceAware;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class NotFoundServlet extends HttpServlet implements Routable, SecurityServiceAware {

    private SecurityService securityService;

    @Override
    public String getMapping() {
        return "/notFound";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    private void redirect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (securityService.isAuthorized(req)) {
            resp.sendRedirect(req.getContextPath() + "/todos");
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        redirect(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        redirect(req, resp);
    }

}
