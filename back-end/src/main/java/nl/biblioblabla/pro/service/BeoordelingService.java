package nl.biblioblabla.pro.service;

import lombok.RequiredArgsConstructor;
import nl.biblioblabla.pro.exception.OngeldigeRatingException;
import nl.biblioblabla.pro.repository.BeoordelingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BeoordelingService {

    private final BeoordelingRepository beoordelingRepository;
    private final StatistiekService statistiekService;

    public void verwerkBeoordeling(int leningId, int gebruikerId, int rating) {

        if (rating < 1 || rating > 5) {
            throw new OngeldigeRatingException("Rating moet tussen 1 en 5 liggen.");
        }

        // DATABASE CALL
        boolean succes = beoordelingRepository.voegBeoordelingToe(leningId, gebruikerId, rating);

        // BUSINESS REGEL
        if (!succes) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Je kunt alleen je eigen leningen beoordelen."
            );
        }

        //Als het opslaan is gelukt, update dan het gemiddelde
        // We geven leningId mee zodat de StatistiekService weet welk boek berekend moet worden
        statistiekService.actualiseerBoekRating(leningId);
    }
}