package nl.biblioblabla.pro.controller;

import lombok.RequiredArgsConstructor;
import nl.biblioblabla.pro.model.Lening;
import nl.biblioblabla.pro.model.User;
import nl.biblioblabla.pro.repository.LeningenRepository;
import nl.biblioblabla.pro.repository.UserRepository;
import nl.biblioblabla.pro.service.BeoordelingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/leningen")
@RequiredArgsConstructor // Genereert automatisch de constructor voor de repositories
public class LeningenController {

    private final LeningenRepository leningenRepository;
    private final UserRepository userRepository;

    @GetMapping("/mijn-overzicht")
    public List<Lening> getMijnLeningen(
            Principal principal,
            @RequestParam(required = false) String titel,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String eind) {

        // 1. Controleer of de gebruiker is ingelogd
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Je moet ingelogd zijn.");
        }

        // 2. Haal de email op uit de Principal en zoek de User in de database
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden.");
        }

        int huidigeGebruikerId = user.getId();

        // 3. Parsen van datums met extra check op lege strings
        LocalDate startDate = (start != null && !start.isEmpty()) ? LocalDate.parse(start) : null;
        LocalDate eindDate = (eind != null && !eind.isEmpty()) ? LocalDate.parse(eind) : null;

        // 4. Haal de gefilterde leningen op voor deze specifieke gebruiker
        return leningenRepository.searchLeningen(huidigeGebruikerId, titel, genre, startDate, eindDate);
    }

    // Voeg deze injectie toe aan je bestaande LeningenController
    private final BeoordelingService beoordelingService;

    @PostMapping("/beoordeel/{leningId}")
    public void rateLening(
            @PathVariable int leningId,
            @RequestParam int rating,
            Principal principal) {

        // 1. Controleer of de gebruiker is ingelogd
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // 2. Haal de user veilig op via de email uit de Principal
        User user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // 3. Voeg de beoordeling toe voor deze specifieke gebruiker
        beoordelingService.verwerkBeoordeling(leningId, user.getId(), rating);
    }
}