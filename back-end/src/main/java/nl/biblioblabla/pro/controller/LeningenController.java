package nl.biblioblabla.pro.controller;

import lombok.RequiredArgsConstructor;
import nl.biblioblabla.pro.model.Lening;
import nl.biblioblabla.pro.model.LeningRequest;
import nl.biblioblabla.pro.model.User;
import nl.biblioblabla.pro.repository.AuteursRepository;
import nl.biblioblabla.pro.repository.BoekenRepository;
import nl.biblioblabla.pro.repository.LeningenRepository;
import nl.biblioblabla.pro.repository.UserRepository;
import nl.biblioblabla.pro.service.BeoordelingService;
import nl.biblioblabla.pro.service.LeningService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/leningen")
@RequiredArgsConstructor
public class LeningenController {

    private final LeningenRepository leningenRepository;
    private final UserRepository userRepository;
    private final BeoordelingService beoordelingService;
    private final AuteursRepository auteursRepository;
    private final BoekenRepository boekenRepository;
    private final LeningService leningService;

    private User getCurrentUser(Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Je moet ingelogd zijn.");
        }

        User user = userRepository.findByEmail(principal.getName());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden.");
        }

        return user;
    }

    @GetMapping("/mijn-overzicht")
    public List<Lening> getMijnLeningen(
            Principal principal,
            @RequestParam(required = false) String titel,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String eind) {

        // Controleer of de gebruiker is ingelogd
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Je moet ingelogd zijn.");
        }

        // Haal de email op uit de Principal en zoek de User in de database
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden.");
        }

        int huidigeGebruikerId = user.getId();

        // Parsen van datums met extra check op lege strings
        LocalDate startDate = (start != null && !start.isBlank()) ? LocalDate.parse(start) : null;
        LocalDate eindDate = (eind != null && !eind.isBlank()) ? LocalDate.parse(eind) : null;

        // Haal de gefilterde leningen op voor deze specifieke gebruiker
        return leningenRepository.searchLeningen(huidigeGebruikerId, titel, genre, startDate, eindDate);
    }

    @PostMapping("/beoordeel/{leningId}")
    public String rateLening(
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

    @PostMapping("/lenen")
    @ResponseStatus(HttpStatus.CREATED)
    public String createLening(@RequestBody LeningRequest request, Principal principal) {

        User user = getCurrentUser(principal);

        leningService.createLening(user.getId(), request);

        return "Lening voor '" + request.getTitel() + "' succesvol geregistreerd!";
    }


    @GetMapping("/suggesties/titels")
    public List<String> getTitelSuggesties(@RequestParam String titel) {
        return boekenRepository.searchTitels(titel);
    }

    @GetMapping("/suggesties/auteurs")
    public List<String> getAuteurSuggesties(@RequestParam String auteur) {
        return auteursRepository.searchAuteurs(auteur);
    }

    @GetMapping("/check-genre")
    public String getGenreVoorTitel(@RequestParam String titelBoek) {
        String genre = boekenRepository.findGenreByTitel(titelBoek);
        if (genre == null) {
            // We sturen een lege string of 404 als het boek nieuw is
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Boek onbekend");
        }
        return genre;
    }
}