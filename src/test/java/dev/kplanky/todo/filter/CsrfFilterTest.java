package dev.kplanky.todo.filter;

import dev.kplanky.todo.service.CsrfService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CsrfFilterTest {

    private CsrfService csrfService;
    private CsrfFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        csrfService = mock(CsrfService.class);
        filter = new CsrfFilter(csrfService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
        when(csrfService.currentToken(any())).thenReturn("a-token");
    }

    @ParameterizedTest(name = "{0} passes through without a token")
    @ValueSource(strings = {"GET", "HEAD", "OPTIONS", "TRACE"})
    @DisplayName("safe methods are not challenged")
    void safeMethodsPass(String method) throws Exception {
        when(request.getMethod()).thenReturn(method);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendError(anyInt(), any());
        verify(csrfService, never()).isValid(any());
    }

    @Test
    @DisplayName("publishes the token to the views so forms can echo it")
    void publishesTokenToViews() throws Exception {
        when(request.getMethod()).thenReturn("GET");

        filter.doFilter(request, response, chain);

        verify(request).setAttribute(CsrfService.PARAMETER, "a-token");
    }

    @ParameterizedTest(name = "{0} without a valid token is refused")
    @ValueSource(strings = {"POST", "PUT", "PATCH", "DELETE"})
    @DisplayName("unsafe methods are refused when the token does not match")
    void unsafeMethodsAreRefused(String method) throws Exception {
        when(request.getMethod()).thenReturn(method);
        when(csrfService.isValid(request)).thenReturn(false);

        filter.doFilter(request, response, chain);

        verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), any());
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    @DisplayName("a refused request never mints a token")
    void refusedRequestMintsNothing() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(csrfService.isValid(request)).thenReturn(false);

        filter.doFilter(request, response, chain);

        verify(csrfService, never()).currentToken(any());
        verify(request, never()).setAttribute(eq(CsrfService.PARAMETER), any());
    }

    @Test
    @DisplayName("a POST carrying the right token proceeds")
    void validTokenProceeds() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(csrfService.isValid(request)).thenReturn(true);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendError(anyInt(), any());
    }

    @Test
    @DisplayName("an unrecognised method is treated as unsafe, not waved through")
    void unknownMethodFailsClosed() throws Exception {
        // Lowercase is a different method per the HTTP spec, and anything not on the safe list
        // must take the checked path rather than the free one.
        when(request.getMethod()).thenReturn("get");
        when(csrfService.isValid(request)).thenReturn(false);

        filter.doFilter(request, response, chain);

        verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), any());
        verify(chain, never()).doFilter(any(), any());
    }

}
