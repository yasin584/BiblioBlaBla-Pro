package nl.biblioblabla.pro.service;

import lombok.RequiredArgsConstructor;
import nl.biblioblabla.pro.repository.BeoordelingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BeoordelingService {

    private final BeoordelingRepository beoordelingRepository;

    public void verwerkBeoordeling(int leningId, int gebruikerId, int rating) {
        // Checklist validatie: alleen waarden tussen 1 en 5
        if (rating < 1 || rating > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating moet tussen 1 en 5 liggen.");
        }

        boolean succes = beoordelingRepository.voegBeoordelingToe(leningId, gebruikerId, rating);

        if (!succes) {
            // Foutmelding als de lening niet bestaat of niet van de gebruiker is
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Je kunt alleen je eigen leningen beoordelen.");
        }
    }
}