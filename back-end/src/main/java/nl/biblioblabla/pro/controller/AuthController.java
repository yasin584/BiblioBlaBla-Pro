package nl.biblioblabla.pro.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import nl.biblioblabla.pro.dto.LoginRequest;
import nl.biblioblabla.pro.dto.LoginResponse;
import nl.biblioblabla.pro.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor // Lombok regelt de dependency injection
public class AuthController {
    
    // Geen @Autowired nodig, gewoon private final
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Laat AuthService de logica afhandelen
            LoginResponse response = authService.login(request);

            // Return HTTP 200 OK met de LoginResponse
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Maak een foutmelding-object aan met de message uit de exception
            AuthErrorResponse errorResponse = new AuthErrorResponse(e.getMessage());

            // Return 401 Unauthorized (via de HttpStatus enum) met de JSON body
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}

@Data
@AllArgsConstructor
class AuthErrorResponse {
    private String error;
}