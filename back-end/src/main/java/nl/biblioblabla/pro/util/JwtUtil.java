package nl.biblioblabla.pro.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 uur

    // Spring haalt de secret uit je application.properties
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Aangepast naar 'email' in plaats van 'username'
    public String generateToken(String email, int userId) {
        return Jwts.builder()
                .subject(email) // De 'sub' claim is nu het e-mailadres
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public int extractUserId(String token) {
        return extractAllClaims(token).get("userId", Integer.class);
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token); // Als dit lukt zonder errors, is de token geldig
            return true;
        } catch (Exception e) {
            return false; // Token is verlopen, ongeldig of verkeerd gesigneerd
        }
    }

    // -- Hulpmethode --
    // Dit voorkomt dat we de parse-logica 3 keer moeten uittypen (DRY principe)
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}