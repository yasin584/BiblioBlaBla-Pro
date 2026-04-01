package nl.biblioblabla.pro.service;

import nl.biblioblabla.pro.model.LoginRequest;
import nl.biblioblabla.pro.model.LoginResponse;
import nl.biblioblabla.pro.model.User;
import nl.biblioblabla.pro.repository.UserRepository;
import nl.biblioblabla.pro.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void tryLogin_metCorrecteGegevens_returnsLoginResponseToken() {
        // Arrange
        String email = "test@email.com";
        String wachtwoord = "testPassword";
        // We maken een hash die we in de 'database' stoppen
        String hashedWachtwoord = passwordEncoder.encode(wachtwoord);
        int gebruikerId = 1;
        String expectedToken = "fake-jwt-token";

        LoginRequest loginRequest = new LoginRequest(email, wachtwoord);

        // Let op: id is een int, dus 1 ipv "1"
        User testUser = new User(gebruikerId, "Fleur", email, hashedWachtwoord);

        // Vertel de mocks wat ze moeten doen
        when(userRepository.findByEmail(email)).thenReturn(testUser);
        when(jwtUtil.generateToken(email, gebruikerId)).thenReturn(expectedToken);

        // --- ACT ---
        LoginResponse response = authService.tryLogin(loginRequest);

        // --- ASSERT ---
        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());
        assertEquals(gebruikerId, response.getGebruikerId());

        // Verifieer of de methodes echt zijn aangeroepen
        verify(userRepository).findByEmail(email);
        verify(jwtUtil).generateToken(email, gebruikerId);
    }

    @Test
    void tryLogin_metNietBekendEmail_throwsRuntimeException() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("nietbestaand@test.com", "wachtwoord123");

        // Mock de repository om null terug te geven
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.tryLogin(loginRequest);
        });

        assertEquals("Ongeldig e-mailadres of wachtwoord", exception.getMessage());

        // Verifieer dat er NOOIT een token is gegenereerd
        verify(jwtUtil, never()).generateToken(anyString(), anyInt());
    }

    @Test
    void tryLogin_metVerkeerdWachtwoord_throwsRuntimeException() {
        // Arrange
        String email = "test@email.com";
        String correctWachtwoord = "geheim123";
        String foutWachtwoord = "foutjeBedankt";

        // De hash in de 'database' is van het GOEDE wachtwoord
        String hashInDb = passwordEncoder.encode(correctWachtwoord);
        User testUser = new User(1, "Fleur", email, hashInDb);

        LoginRequest loginRequest = new LoginRequest(email, foutWachtwoord);

        when(userRepository.findByEmail(email)).thenReturn(testUser);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.tryLogin(loginRequest));

        // Verifieer wederom dat er geen token is gemaakt
        verify(jwtUtil, never()).generateToken(anyString(), anyInt());
    }

}