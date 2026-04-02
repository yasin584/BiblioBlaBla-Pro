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
    private final BeoordelingService beoordelingService;

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
        LocalDate startDate = (start != null && !start.isBlank()) ? LocalDate.parse(start) : null;
        LocalDate eindDate = (eind != null && !eind.isBlank()) ? LocalDate.parse(eind) : null;

        // 4. Haal de gefilterde leningen op voor deze specifieke gebruiker
        return leningenRepository.searchLeningen(huidigeGebruikerId, titel, genre, startDate, eindDate);
    }

    @PostMapping("/beoordeel/{leningId}")
    public String rateLening( // Verander 'void' naar 'String'
                              @PathVariable int leningId,
                              @RequestParam int rating,
                              Principal principal) {

        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findByEmail(principal.getName());

        // Voer de beoordeling uit
        beoordelingService.verwerkBeoordeling(leningId, user.getId(), rating);

        // Dit bericht zie je straks in het grote witte vlak van Insomnia
        return "Succes! Je hebt dit boek een beoordeling van " + rating + " gegeven.";
    }
}