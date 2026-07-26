package dev.kplanky.todo.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Synchronizer tokens for the state-changing forms. A cross-site page can make a browser POST to
 * this app, and SameSite=Lax on the session cookie is what currently stops that being useful, but
 * that is a browser default rather than something the app enforces. This is the enforcement: a
 * secret held in the session, echoed back by the form, compared on every unsafe request.
 *
 * The token is per session, not per form. That is enough for a synchronizer token, and it means
 * two tabs open on the same account do not invalidate each other.
 */
public final class CsrfService {

    /** Form field and request-attribute name. */
    public static final String PARAMETER = "csrfToken";

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int TOKEN_BYTES = 32;

    /**
     * The token for this session, minting one on first use. Creates a session if there is none,
     * which is deliberate: the login and register forms need a token before anyone is logged in.
     */
    public String currentToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object existing = session.getAttribute(SessionKeys.CSRF_TOKEN);
        if (existing instanceof String token && !token.isBlank()) {
            return token;
        }
        byte[] bytes = new byte[TOKEN_BYTES];
        RANDOM.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        session.setAttribute(SessionKeys.CSRF_TOKEN, token);
        return token;
    }

    /**
     * True when the request carries the session's token. Absent session, absent attribute or
     * absent parameter all fail, so a request that predates the token being issued is rejected
     * rather than waved through.
     */
    public boolean isValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        Object expected = session.getAttribute(SessionKeys.CSRF_TOKEN);
        String submitted = request.getParameter(PARAMETER);
        if (!(expected instanceof String expectedToken) || submitted == null) {
            return false;
        }
        return MessageDigest.isEqual(
                expectedToken.getBytes(StandardCharsets.UTF_8),
                submitted.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Drops the token so the next issue mints a fresh one. Called on login, where the session id
     * is rotated too: a token minted before authentication should not outlive it.
     */
    public void reset(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(SessionKeys.CSRF_TOKEN);
        }
    }

}
