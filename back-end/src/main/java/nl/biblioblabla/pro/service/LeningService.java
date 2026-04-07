package nl.biblioblabla.pro.service;

import lombok.RequiredArgsConstructor;
import nl.biblioblabla.pro.model.LeningRequest;
import nl.biblioblabla.pro.repository.AuteursRepository;
import nl.biblioblabla.pro.repository.BoekenRepository;
import nl.biblioblabla.pro.repository.LeningenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LeningService {

    private final LeningenRepository leningenRepository;
    private final AuteursRepository auteursRepository;
    private final BoekenRepository boekenRepository;

    public void createLening(int userId, LeningRequest request) {

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

        if (request.getInleverdatum().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inleverdatum mag niet in het verleden liggen");
        }

        int auteurId = auteursRepository.getOrCreateAuteur(request.getAuteur());

        int boekId = boekenRepository.getOrCreateBoek(
                request.getTitel(),
                request.getGenre(),
                auteurId
        );

        leningenRepository.saveLening(userId, boekId, request.getInleverdatum());
    }
}