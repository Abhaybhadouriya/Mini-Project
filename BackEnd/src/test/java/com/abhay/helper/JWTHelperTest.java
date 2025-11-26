package com.abhay.helper;

import com.abhay.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JWTHelperTest {

    private JWTHelper jwtHelper;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        jwtHelper = new JWTHelper();

        testCustomer = Customer.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .rollNumber("R101")
                .build();
    }

    @Test
    void testGenerateAndValidateToken() {
        String token = jwtHelper.generateToken(testCustomer);
        assertNotNull(token);

        assertTrue(jwtHelper.validateToken(token));

        assertEquals("test@example.com", jwtHelper.extractUsername(token));
        // FIX: Explicitly cast expected value to Long
        assertEquals((Long) 1L, jwtHelper.extractClaim(token, claims -> claims.get("id", Long.class)));
    }

    @Test
    void testTokenExpiration() { // FIX: Removed unnecessary 'throws InterruptedException'
        String token = jwtHelper.generateToken(testCustomer);

        Date expirationDate = jwtHelper.extractExpiration(token);
        assertTrue(expirationDate.after(Date.from(Instant.now())));
    }

    // NEW TEST: KILLS MUTANT IN THE CATCH BLOCK (Illegal Token Format)
    @Test
    void testValidateToken_ShouldReturnFalseForInvalidFormat() {
        assertFalse(jwtHelper.validateToken("invalid.jwt.string"));
        assertFalse(jwtHelper.validateToken(""));
        assertFalse(jwtHelper.validateToken(null));
    }

    // NEW TEST: KILLS MUTANT IN THE CATCH BLOCK (Bad Signature/Validation Logic)
    @Test
    void testValidateToken_ShouldReturnFalseForTokenWithBadSignature() {
        // Create a valid token, but tamper with its content (header or claims) to invalidate signature.
        // Since we can't easily tamper with the signature, we'll use a token known to be invalid for another reason (e.g. malformed JWT structure).
        // This ensures the exception handler in JWTHelper.validateToken() is hit by different types of parsing errors.
        String tamperedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SFLSwsxP8-j9u4c_kF_Y6i8u9Yg8Q7I-V0vM-rT6u4U"; // A real but incorrectly signed token for the helper's key
        assertFalse(jwtHelper.validateToken(tamperedToken));
    }
}