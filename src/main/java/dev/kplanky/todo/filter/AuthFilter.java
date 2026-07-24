package dev.kplanky.todo.filter;

import dev.kplanky.todo.service.SecurityService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthFilter implements Filter {

    private final SecurityService securityService;

    public AuthFilter(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String context = req.getContextPath();
        String path = req.getRequestURI().substring(context.length());

        boolean authed = securityService.isAuthorized(req);
        boolean publicPath = path.equals("/login") || path.equals("/register");

        if (!authed && !publicPath) {
            resp.sendRedirect(context + "/login");
            return;
        }
        if (authed && path.equals("/")) {
            resp.sendRedirect(context + "/todos");
            return;
        }
        chain.doFilter(request, response);
    }

}
