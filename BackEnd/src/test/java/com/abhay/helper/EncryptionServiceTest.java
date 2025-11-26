package com.abhay.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EncryptionServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        encryptionService = new EncryptionService(passwordEncoder);
    }

    @Test
    void testEncode() {
        String rawPassword = "securePassword";
        String encodedPassword = "hashedValue123";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        String result = encryptionService.encode(rawPassword);

        assertEquals(encodedPassword, result);
        verify(passwordEncoder, times(1)).encode(rawPassword);
    }

    @Test
    void testValidates_Success() {
        String rawPassword = "securePassword";
        String encodedPassword = "hashedValue123";

        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        assertTrue(encryptionService.validates(rawPassword, encodedPassword));

        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }

    @Test
    void testValidates_Failure() {
        String rawPassword = "wrongPassword";
        String encodedPassword = "hashedValue123";

        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        assertFalse(encryptionService.validates(rawPassword, encodedPassword));

        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }
}