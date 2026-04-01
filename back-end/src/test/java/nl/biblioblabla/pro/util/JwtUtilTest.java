package nl.biblioblabla.pro.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    // Een veilige test-sleutel (minimaal 32 tekens voor HS256)
    private final String secret = "mijnSuperGeheimeTestSleutelVanMinimaal32Tekens";

    @BeforeEach
    void setUp() {
        // We maken handmatig een instantie aan en injecteren de secret
        jwtUtil = new JwtUtil(secret);
    }

    @Test
    void generateToken_shouldCreateValidToken() {
        // Arrange
        String email = "test@biblio.nl";
        int userId = 42;

        // Act
        String token = jwtUtil.generateToken(email, userId);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void extractEmail_shouldReturnCorrectEmail() {
        // Arrange
        String expectedEmail = "user@example.com";
        String token = jwtUtil.generateToken(expectedEmail, 1);

        // Act
        String extractedEmail = jwtUtil.extractEmail(token);

        // Assert
        assertEquals(expectedEmail, extractedEmail);
    }

    @Test
    void extractUserId_shouldReturnCorrectId() {
        // Arrange
        int expectedId = 99;
        String token = jwtUtil.generateToken("test@test.com", expectedId);

        // Act
        int extractedId = jwtUtil.extractUserId(token);

        // Assert
        assertEquals(expectedId, extractedId);
    }

    @Test
    void validateToken_givenInvalidToken_shouldReturnFalse() {
        // Arrange
        String invalidToken = "dit.is.geentoken";

        // Act
        boolean isValid = jwtUtil.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_givenExpiredOrTamperedToken_shouldReturnFalse() {
        // Arrange
        String token = jwtUtil.generateToken("test@test.com", 1);
        // We veranderen één letter in de token (tampering)
        String tamperedToken = token.substring(0, token.length() - 1) + "A";

        // Act
        boolean isValid = jwtUtil.validateToken(tamperedToken);

        // Assert
        assertFalse(isValid);
    }

}