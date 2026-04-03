package nl.biblioblabla.pro.service;

import lombok.RequiredArgsConstructor;
import nl.biblioblabla.pro.model.GebruikerStats;
import nl.biblioblabla.pro.repository.StatistiekRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatistiekService {
    private final StatistiekRepository statistiekRepository;

    public GebruikerStats getGebruikerDashboard(int gebruikerId) {
        GebruikerStats stats = new GebruikerStats();

        // 1. Totaal aantal geleende boeken ophalen
        stats.setTotaalGeleend(statistiekRepository.getTotaalGeleend(gebruikerId));

        // 2. Populairste genre ophalen (voeg deze regel toe)
        String populairste = statistiekRepository.getPopulairsteGenre(gebruikerId);
        stats.setPopulairsteGenre(populairste);

        return stats;
    }

    public void actualiseerBoekRating(int leningId) {
        // 1. Zoek eerst het boekId op dat bij deze lening hoort
        int boekId = statistiekRepository.getBoekIdBijLening(leningId);

        // 2. Bereken het gemiddelde voor dat specifieke boek
        Double nieuwGemiddelde = statistiekRepository.berekenGemiddeldeVoorBoek(boekId);

        if (nieuwGemiddelde != null) {
            statistiekRepository.updateBoekGemiddelde(boekId, nieuwGemiddelde);
        }
    }
}