package dev.kplanky.todo.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CsrfServiceTest {

    private CsrfService csrfService;
    private HttpServletRequest request;
    private HttpSession session;
    private Map<String, Object> attributes;

    @BeforeEach
    void setUp() {
        csrfService = new CsrfService();
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        attributes = new HashMap<>();

        // A session that behaves like a real attribute map, so token storage is actually exercised.
        when(session.getAttribute(any())).thenAnswer(i -> attributes.get(i.getArgument(0, String.class)));
        doAnswer(i -> attributes.put(i.getArgument(0), i.getArgument(1)))
                .when(session).setAttribute(any(), any());
        doAnswer(i -> attributes.remove(i.getArgument(0, String.class)))
                .when(session).removeAttribute(any());

        when(request.getSession()).thenReturn(session);
        when(request.getSession(false)).thenReturn(session);
    }

    @Test
    @DisplayName("mints a token and stores it on the session")
    void mintsAndStores() {
        String token = csrfService.currentToken(request);

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertEquals(token, attributes.get(SessionKeys.CSRF_TOKEN));
    }

    @Test
    @DisplayName("the token has enough entropy to be unguessable")
    void hasEntropy() {
        // 32 random bytes, url-safe base64 without padding.
        assertEquals(43, csrfService.currentToken(request).length());
    }

    @Test
    @DisplayName("returns the same token for the same session, so two tabs do not fight")
    void stableWithinASession() {
        assertEquals(csrfService.currentToken(request), csrfService.currentToken(request));
    }

    @Test
    @DisplayName("two sessions get different tokens")
    void differsAcrossSessions() {
        String first = csrfService.currentToken(request);

        attributes.clear();   // a different session has its own attribute map
        String second = csrfService.currentToken(request);

        assertNotEquals(first, second);
    }

    @Test
    @DisplayName("accepts the request that echoes the session's token")
    void acceptsMatchingToken() {
        String token = csrfService.currentToken(request);
        when(request.getParameter(CsrfService.PARAMETER)).thenReturn(token);

        assertTrue(csrfService.isValid(request));
    }

    @Test
    @DisplayName("rejects a wrong, missing or empty token")
    void rejectsBadToken() {
        csrfService.currentToken(request);

        when(request.getParameter(CsrfService.PARAMETER)).thenReturn("forged");
        assertFalse(csrfService.isValid(request), "a guessed token must not pass");

        when(request.getParameter(CsrfService.PARAMETER)).thenReturn(null);
        assertFalse(csrfService.isValid(request), "an absent parameter must not pass");

        when(request.getParameter(CsrfService.PARAMETER)).thenReturn("");
        assertFalse(csrfService.isValid(request), "an empty parameter must not pass");
    }

    @Test
    @DisplayName("rejects when there is no session at all")
    void rejectsWithoutSession() {
        when(request.getSession(false)).thenReturn(null);
        when(request.getParameter(CsrfService.PARAMETER)).thenReturn("anything");

        assertFalse(csrfService.isValid(request));
    }

    @Test
    @DisplayName("rejects a session that never had a token issued")
    void rejectsWhenNoTokenWasIssued() {
        when(request.getParameter(CsrfService.PARAMETER)).thenReturn("anything");

        assertFalse(csrfService.isValid(request), "must fail closed, not wave the request through");
    }

    @Test
    @DisplayName("a token from one session does not work against another")
    void tokenIsBoundToItsSession() {
        String stolen = csrfService.currentToken(request);

        attributes.clear();
        csrfService.currentToken(request);            // the victim's session mints its own
        when(request.getParameter(CsrfService.PARAMETER)).thenReturn(stolen);

        assertFalse(csrfService.isValid(request));
    }

    @Test
    @DisplayName("reset drops the token so login mints a fresh one")
    void resetRotatesTheToken() {
        String before = csrfService.currentToken(request);

        csrfService.reset(request);

        assertFalse(attributes.containsKey(SessionKeys.CSRF_TOKEN));
        assertNotEquals(before, csrfService.currentToken(request));
    }

    @Test
    @DisplayName("reset on a sessionless request is a no-op, not a crash")
    void resetWithoutSessionIsSafe() {
        when(request.getSession(false)).thenReturn(null);

        assertDoesNotThrow(() -> csrfService.reset(request));
        verify(session, never()).removeAttribute(eq(SessionKeys.CSRF_TOKEN));
    }

}
