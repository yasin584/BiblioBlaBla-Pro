package nl.biblioblabla.pro.service;

import nl.biblioblabla.pro.model.LoginRequest;
import nl.biblioblabla.pro.model.LoginResponse;
import nl.biblioblabla.pro.model.User;
import nl.biblioblabla.pro.repository.UserRepository;
import nl.biblioblabla.pro.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // BCryptPasswordEncoder controleert of een plain-text wachtwoord overeenkomt met de hash
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /* LoginResponse:
     hoofdmethode die het hele inlogproces coördineert
     LoginRequest request: Bevat de email en het wachtwoord van de frontend
     als inloggen lukt: returnt LoginResponse met JWT token en gebruikerId */
    public LoginResponse tryLogin(LoginRequest loginRequest) {

        // Gebruiker vinden via email
        User user = userRepository.findByEmail(loginRequest.getEmail());
        
        if (user == null) {
            throw new RuntimeException("Ongeldig e-mailadres of wachtwoord");
        }

        // Verifieer het wachtwoord
        boolean passwordMatches = passwordEncoder.matches(
                loginRequest.getWachtwoord(),
                user.getWachtwoordHash()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Ongeldig e-mailadres of wachtwoord");
        }

        // Als wachtwoord klopt genereer JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        // Maak en retourneer LoginResponse (bevat token en gebruikerId voor React)
        return new LoginResponse(token, user.getId());
    }
}