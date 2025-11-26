package com.abhay.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RequestInterceptor implements HandlerInterceptor {
    private final JWTHelper jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            return true; // Allow preflight requests
        }

        String uri = request.getRequestURI();
        if (uri.contains("/api/students/register") || uri.contains("/api/auth/login")) {
            return true; // Skip authorization check for specific endpoints
        }

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            setResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "You are not Authorized for this method");            return false;
        }

        String token = authorizationHeader.substring(7); // Extract token from "Bearer {token}"
//        System.out.println(token);

        if (!jwtUtil.validateToken(token)) {
            setResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");            return false;
        }
        String username = jwtUtil.extractUsername(token);

        if (username == null) {
            setResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");            return false;
        }

        return true;
    }

    private void setResponse(HttpServletResponse response, int status, String message) throws Exception {
        response.setStatus(status);
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(
                Map.of(
                        "status", status,
                        "message", message
                )
        );
        response.getWriter().write(jsonResponse);
    }


}
