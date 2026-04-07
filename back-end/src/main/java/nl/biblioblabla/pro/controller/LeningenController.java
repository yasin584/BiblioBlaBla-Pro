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

    @GetMapping("/mijn-overzicht")
    public List<Lening> getMijnLeningen(
            Principal principal, //object dat Spring je geeft om te weten wie er is ingelogd
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden."); //terug geeft (HTTP 404)
        }

        int huidigeGebruikerId = user.getId();

        // Parsen van datums met extra check op lege strings
        LocalDate startDate = null;
        if (start != null && !start.isBlank())  {
            startDate = LocalDate.parse(start);
        }

        //isBlank() Java String controleert of een string leeg is of alleen uit witruimtes bestaat.

        LocalDate eindDate = null;
        if (eind != null && !eind.isBlank()) {
            eindDate = LocalDate.parse(eind);
        }

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

        User user = userRepository.findByEmail(principal.getName()); //geeft de email van de ingelogde gebruiker

        // Voer de beoordeling uit
        beoordelingService.verwerkBeoordeling(leningId, user.getId(), rating);

        return "Succes! Je hebt dit boek een beoordeling van " + rating + " gegeven.";
    }

    @PostMapping("/lenen")
    @ResponseStatus(HttpStatus.CREATED)
    public String createLening(@RequestBody LeningRequest request, Principal principal) {
        // Controleer of de gebruiker is ingelogd
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Je moet ingelogd zijn.");
        }

        // Validatie
        if (request.getTitel() == null || request.getTitel().length() < 3 || request.getTitel().length() > 30) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Titel moet tussen de 3 en 30 tekens zijn.");
        }

        if (request.getAuteur() == null || request.getAuteur().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Auteur is verplicht.");
        }

        if (request.getGenre() == null || request.getGenre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Genre is verplicht.");
        }

        if (request.getInleverdatum() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inleverdatum is verplicht.");
        }

        // Haal de gebruiker op
        User user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden.");
        }

        // Haal auteur op of maak aan
        int auteurId = auteursRepository.getOrCreateAuteur(request.getAuteur());

        // Haal boek op of maak aan
        int boekId = boekenRepository.getOrCreateBoek(request.getTitel(), request.getGenre(), auteurId);

        // De lening opslaan in de database
        leningenRepository.saveLening(user.getId(), boekId, request.getInleverdatum());

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