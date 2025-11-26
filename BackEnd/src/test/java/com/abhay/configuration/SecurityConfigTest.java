package com.abhay.configuration;

import com.abhay.helper.RequestInterceptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private RequestInterceptor requestInterceptor;

    @Mock
    private InterceptorRegistry interceptorRegistry;

    @Mock
    private InterceptorRegistration interceptorRegistration;

    @Mock
    private CorsRegistry corsRegistry;

    @Mock
    private CorsRegistration corsRegistration;

    @Test
    void testPasswordEncoder_ReturnsBCryptPasswordEncoder() {
        SecurityConfig config = new SecurityConfig(requestInterceptor);
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder);
    }

    @Test
    void testAddInterceptors_RegistersInterceptorAndExcludesPaths() {
        when(interceptorRegistry.addInterceptor(requestInterceptor)).thenReturn(interceptorRegistration);
        when(interceptorRegistration.addPathPatterns(any(String[].class))).thenReturn(interceptorRegistration);
        when(interceptorRegistration.excludePathPatterns(any(String[].class))).thenReturn(interceptorRegistration);

        SecurityConfig config = new SecurityConfig(requestInterceptor);
        config.addInterceptors(interceptorRegistry);

        verify(interceptorRegistry, times(1)).addInterceptor(requestInterceptor);
        verify(interceptorRegistration, times(1)).addPathPatterns("/**");
        verify(interceptorRegistration, times(1)).excludePathPatterns("/api/auth/login", "/api/students/register", "/api/students/register/");
    }

    @Test
    void testAddCorsMappings_ConfiguresCors() {
        when(corsRegistry.addMapping(anyString())).thenReturn(corsRegistration);
        when(corsRegistration.allowedOrigins(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowedHeaders(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.exposedHeaders(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowCredentials(anyBoolean())).thenReturn(corsRegistration);


        SecurityConfig config = new SecurityConfig(requestInterceptor);
        config.addCorsMappings(corsRegistry);

        verify(corsRegistry, times(1)).addMapping("/**");
        verify(corsRegistration, times(1)).allowedOrigins("http://localhost:3000");
        verify(corsRegistration, times(1)).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        verify(corsRegistration, times(1)).allowedHeaders("Content-Type", "Authorization");
        verify(corsRegistration, times(1)).exposedHeaders("Authorization");
        verify(corsRegistration, times(1)).allowCredentials(true);
    }
}