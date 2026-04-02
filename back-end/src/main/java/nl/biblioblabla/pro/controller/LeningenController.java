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
@RequiredArgsConstructor // Genereert automatisch de constructor voor de repositories
public class LeningenController {

    private final LeningenRepository leningenRepository;
    private final UserRepository userRepository;
    private final BeoordelingService beoordelingService;
    private final AuteursRepository auteursRepository;
    private final BoekenRepository boekenRepository;

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

    @PostMapping("/lenen")
    @ResponseStatus(HttpStatus.CREATED)
    public String createLening(@RequestBody LeningRequest request, Principal principal) {
        // 1. Controleer of de gebruiker is ingelogd
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Je moet ingelogd zijn.");
        }

        // 2. Validatie (Titel check uit screenshot)
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

        // 3. Haal de gebruiker op
        User user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden.");
        }

        // 4. Gebruik de repositories (Hierdoor verdwijnen de 'never used' meldingen!)
        // Haal auteur op of maak aan
        int auteurId = auteursRepository.getOrCreateAuteur(request.getAuteur());

        // Haal boek op of maak aan
        int boekId = boekenRepository.getOrCreateBoek(request.getTitel(), request.getGenre(), auteurId);

        // 5. De lening opslaan in de database
        // Hiervoor roepen we de nieuwe methode in LeningenRepository aan (zie stap hieronder)
        leningenRepository.saveLening(user.getId(), boekId, request.getInleverdatum());

        return "Lening voor '" + request.getTitel() + "' succesvol geregistreerd!";
    }

    // SUGGESTIE ENDPOINTS (Voor de frontend autocomplete) ---

    @GetMapping("/suggesties/titels")
    public List<String> getTitelSuggesties(@RequestParam String titel) {
        // q is de zoekterm die de gebruiker typt
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