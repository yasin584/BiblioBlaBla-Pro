package nl.biblioblabla.pro.config;

import static org.junit.jupiter.api.Assertions.*;
import nl.biblioblabla.pro.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        // Zorg dat de SecurityContext leeg is voor elke test
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        // Opruimen na elke test zodat tests elkaar niet beïnvloeden
        SecurityContextHolder.clearContext();
    }

    //-----------------------------------------------
    // Scenario 1: Geen Authorization header aanwezig
    //-----------------------------------------------

    @Test
    @DisplayName("Geen Authorization header → filter gaat direct door, geen authenticatie gezet")
    void geenAuthorizationHeader_gaatDoor() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        // FilterChain moet doorgaan
        verify(filterChain).doFilter(request, response);
        // SecurityContext moet leeg blijven
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        // JwtUtil mag niet worden aangeroepen
        verifyNoInteractions(jwtUtil);
    }

    // -------------------------------------------------------------------------
    // Scenario 2: Authorization header aanwezig maar ZONDER "Bearer " prefix
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Authorization header zonder 'Bearer ' prefix → filter gaat door, geen authenticatie")
    void authorizationHeaderZonderBearerPrefix_gaatDoor() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNz");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verifyNoInteractions(jwtUtil);
    }

    // -------------------------------------------------------------------------
    // Scenario 3: Geldig Bearer token → gebruiker wordt geauthenticeerd
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Geldig Bearer token → authenticatie wordt gezet in SecurityContext")
    void geldigBearerToken_authenticatieWordtGezet() throws ServletException, IOException {
        String token = "geldig.jwt.token";
        String email = "jan@voorbeeld.nl";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractEmail(token)).thenReturn(email);

        filter.doFilterInternal(request, response, filterChain);

        // FilterChain moet doorgaan
        verify(filterChain).doFilter(request, response);

        // SecurityContext moet de authenticatie bevatten
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getPrincipal()).isEqualTo(email);
        assertThat(auth.getCredentials()).isNull();
        assertThat(auth.getAuthorities()).isEmpty();
    }

    // -------------------------------------------------------------------------
    // Scenario 4: Ongeldig token (validateToken geeft false terug)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Ongeldig token → geen authenticatie gezet, filter gaat wel door")
    void ongeldigToken_geenAuthenticatieGezet() throws ServletException, IOException {
        String token = "ongeldig.jwt.token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        // extractEmail mag NIET worden aangeroepen als validatie faalt
        verify(jwtUtil, never()).extractEmail(any());
    }

    // -------------------------------------------------------------------------
    // Scenario 5: JwtUtil gooit een exception (verlopen of corrupt token)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("JwtUtil gooit exception → filter gaat door, geen authenticatie, geen crash")
    void jwtUtilGooitException_filterGaatDoor() throws ServletException, IOException {
        String token = "verlopen.jwt.token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenThrow(new RuntimeException("Token verlopen"));

        // Mag geen exception gooien naar de caller
        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    // -------------------------------------------------------------------------
    // Scenario 6: Lege Authorization header
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Lege Authorization header → filter gaat door, geen authenticatie")
    void legeAuthorizationHeader_gaatDoor() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verifyNoInteractions(jwtUtil);
    }

    // -------------------------------------------------------------------------
    // Scenario 7: Token met extra spaties (edge case)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("'Bearer ' header met leeg token gedeelte → validateToken wordt aangeroepen met lege string")
    void bearerHeaderMetLeegToken_validateTokenWordtAangeroepen() throws ServletException, IOException {
        // "Bearer " + lege string → token = ""
        when(request.getHeader("Authorization")).thenReturn("Bearer ");
        when(jwtUtil.validateToken("")).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtUtil).validateToken("");
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    // -------------------------------------------------------------------------
    // Scenario 8: extractEmail gooit exception na succesvolle validatie
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("extractEmail gooit exception → geen authenticatie gezet, filter gaat door")
    void extractEmailGooitException_filterGaatDoor() throws ServletException, IOException {
        String token = "geldig.maar.corrupt.token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractEmail(token)).thenThrow(new RuntimeException("Claims kunnen niet worden gelezen"));

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

}