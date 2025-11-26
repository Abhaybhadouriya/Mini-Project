package com.abhay.helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestInterceptorTest {

    @Mock
    private JWTHelper jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter mockWriter;

    @InjectMocks
    private RequestInterceptor requestInterceptor;

    // Helper method to set up mocks for denial paths
    private void setupDenialMocks() throws Exception {
        // Stub the response writer ONLY in tests that need to verify output (denial paths)
        when(response.getWriter()).thenReturn(mockWriter);
    }

    @Test
    void initializationTest() {
        // Explicitly calling the constructor ensures Pitest marks it as covered.
        RequestInterceptor interceptor = new RequestInterceptor(jwtUtil);
        assertNotNull(interceptor);
    }

    @Test
    void afterCompletionTest() throws Exception {
        // Calls the empty method to ensure line coverage
        requestInterceptor.afterCompletion(request, response, new Object(), null);

        verifyNoMoreInteractions(request, response, jwtUtil);
    }

    @Test
    void preHandle_ShouldAllowOptionsRequest() throws Exception {
        when(request.getMethod()).thenReturn("OPTIONS");
        assertTrue(requestInterceptor.preHandle(request, response, new Object()));
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void preHandle_ShouldAllowLoginPath() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/auth/login");
        assertTrue(requestInterceptor.preHandle(request, response, new Object()));
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void preHandle_ShouldAllowRegisterPath() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/students/register");
        assertTrue(requestInterceptor.preHandle(request, response, new Object()));
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void preHandle_ShouldDenyIfNoAuthorizationHeader() throws Exception {
        setupDenialMocks(); // Set up writer stubbing
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/secure/data");
        when(request.getHeader("Authorization")).thenReturn(null);

        assertFalse(requestInterceptor.preHandle(request, response, new Object()));

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(mockWriter).write(contains("You are not Authorized"));
    }

    @Test
    void preHandle_ShouldDenyIfAuthorizationHeaderDoesNotStartWithBearer() throws Exception {
        setupDenialMocks(); // Set up writer stubbing
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/secure/data");
        when(request.getHeader("Authorization")).thenReturn("Basic 12345");

        assertFalse(requestInterceptor.preHandle(request, response, new Object()));

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(mockWriter).write(contains("You are not Authorized"));
    }

    @Test
    void preHandle_ShouldDenyIfTokenIsInvalid() throws Exception {
        setupDenialMocks(); // Set up writer stubbing
        String invalidToken = "invalid.jwt.token";

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/secure/data");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);
        when(jwtUtil.validateToken(invalidToken)).thenReturn(false);

        assertFalse(requestInterceptor.preHandle(request, response, new Object()));

        verify(jwtUtil).validateToken(invalidToken);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(mockWriter).write(contains("Invalid or expired token"));
    }

    @Test
    void preHandle_ShouldDenyIfUsernameIsNull() throws Exception {
        setupDenialMocks(); // Set up writer stubbing
        String validToken = "valid.jwt.token";

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/secure/data");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.extractUsername(validToken)).thenReturn(null);

        assertFalse(requestInterceptor.preHandle(request, response, new Object()));

        verify(jwtUtil).extractUsername(validToken);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(mockWriter).write(contains("Invalid or expired token"));
    }

    @Test
    void preHandle_ShouldAllowIfTokenIsValid() throws Exception {
        String validToken = "valid.jwt.token";
        String username = "test@example.com";

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/secure/data");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.extractUsername(validToken)).thenReturn(username);

        assertTrue(requestInterceptor.preHandle(request, response, new Object()));
    }
}