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
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Kijk of er een Authorization header is
        String authHeader = request.getHeader("Authorization");

        // Als die er niet is, of niet begint met Bearer ga dan direct door
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Haal Bearer (7 tekens) eraf om de pure token te krijgen
        String token = authHeader.substring(7);

        try {
            // Valideer de token via JwtUtil
            if (jwtUtil.validateToken(token)) {

                String email = jwtUtil.extractEmail(token);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of()
                        );

                // Sla het authenticatie-object op in de Security Context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            System.err.println("JWT validatie mislukt: " + e.getMessage());
        }
        
        // Laat het verzoek door naar het volgende station (bijv. je Controller)
        filterChain.doFilter(request, response);
    }
}