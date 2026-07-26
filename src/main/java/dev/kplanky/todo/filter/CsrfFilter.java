package dev.kplanky.todo.filter;

import dev.kplanky.todo.service.CsrfService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

/**
 * Rejects unsafe requests that do not carry the session's CSRF token, and publishes the token to
 * the views as a request attribute so every form can echo it back.
 *
 * Mapped ahead of {@link AuthFilter} so that a forged POST is refused outright rather than being
 * redirected to the login page, and so /login and /register are covered too: login CSRF is real,
 * an attacker who can log a victim into an account they control can then watch what the victim
 * does in it.
 */
public class CsrfFilter implements Filter {

    private static final Set<String> SAFE_METHODS = Set.of("GET", "HEAD", "OPTIONS", "TRACE");

    private final CsrfService csrfService;

    public CsrfFilter(CsrfService csrfService) {
        this.csrfService = csrfService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (!SAFE_METHODS.contains(req.getMethod()) && !csrfService.isValid(req)) {
            // A stale form after a session timeout lands here as well as a genuine forgery, hence
            // the wording. 403 is correct either way: the request is refused, not retried.
            resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Your form expired or could not be verified. Reload the page and try again.");
            return;
        }

        // After the check, so a rejected request never mints a token, and on every safe request,
        // so any page that renders a form has one available.
        req.setAttribute(CsrfService.PARAMETER, csrfService.currentToken(req));

        chain.doFilter(request, response);
    }

}
