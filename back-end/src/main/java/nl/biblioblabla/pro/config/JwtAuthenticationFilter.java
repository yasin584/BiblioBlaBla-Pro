package nl.biblioblabla.pro.config;

import lombok.RequiredArgsConstructor;
import nl.biblioblabla.pro.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor // Lombok regelt de constructor voor JwtUtil!
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Kijk of er een Authorization header is
        String authHeader = request.getHeader("Authorization");

        // 2. Als die er niet is, of niet begint met "Bearer ", ga dan direct door 
        // (Spring Security zal het verzoek daarna zelf blokkeren als het endpoint beveiligd is)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Knip "Bearer " (7 tekens) eraf om de pure token te krijgen
        String token = authHeader.substring(7);

        try {
            // 4. Valideer de token via onze JwtUtil
            if (jwtUtil.validateToken(token)) {

                // 5. Haal het E-MAILADRES uit de token (aangepast van username naar email!)
                String email = jwtUtil.extractEmail(token);

                // 6. Vertel Spring Security wie er zojuist is goedgekeurd.
                // We gebruiken List.of() in plaats van null voor de authorities om NullPointerExceptions te voorkomen.
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of() // Lege lijst met rollen/rechten
                        );

                // 7. Sla het authenticatie-object op in de Security Context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // In de praktijk wil je hier liever een log.error() gebruiken in plaats van System.err
            System.err.println("JWT validatie mislukt: " + e.getMessage());
        }
        
        // 8. Laat het verzoek door naar het volgende station (bijv. je Controller)
        filterChain.doFilter(request, response);
    }
}