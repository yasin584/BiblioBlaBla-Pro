package nl.biblioblabla.pro.service;

import lombok.RequiredArgsConstructor;
import nl.biblioblabla.pro.dto.LoginRequest;
import nl.biblioblabla.pro.dto.LoginResponse;
import nl.biblioblabla.pro.model.User;
import nl.biblioblabla.pro.repository.UserRepository;
import nl.biblioblabla.pro.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Lombok verzorgt automatisch de dependency injection voor final fields
public class AuthService {
    
    // Geen @Autowired meer nodig, Lombok maakt de constructor
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // BCryptPasswordEncoder controleert of een plain-text wachtwoord overeenkomt met de hash
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /* LoginResponse:
     hoofdmethode die het hele inlogproces coördineert
     LoginRequest request: Bevat de email en het wachtwoord van de frontend
     als inloggen lukt: returnt LoginResponse met JWT token en gebruikerId */
    public LoginResponse login(LoginRequest request) {

        // 1: Probeer de gebruiker in de database te vinden via EMAIL (niet username!)
        User user = userRepository.findByEmail(request.getEmail());
        
        if (user == null) {
            // Geef nooit exact aan of het e-mailadres of het wachtwoord fout is (veiligheidsreden)
            throw new RuntimeException("Ongeldig e-mailadres of wachtwoord");
        }

        // 2: Verifieer het wachtwoord
        // request.getWachtwoord() haalt plain-text op uit de DTO
        // user.getWachtwoordHash() haalt de opgeslagen hash op uit de DB
        boolean passwordMatches = passwordEncoder.matches(
                request.getWachtwoord(),
                user.getWachtwoordHash()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Ongeldig e-mailadres of wachtwoord");
        }

        // 3: Als wachtwoord klopt: genereer JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        // 4: Maak en retourneer LoginResponse (bevat token en gebruikerId voor React)
        return new LoginResponse(token, user.getId());
    }
}