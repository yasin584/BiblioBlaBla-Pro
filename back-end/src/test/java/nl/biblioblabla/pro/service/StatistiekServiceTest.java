package nl.biblioblabla.pro.service;

import nl.biblioblabla.pro.model.GebruikerStats;
import nl.biblioblabla.pro.repository.StatistiekRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatistiekServiceTest {

    @Mock
    private StatistiekRepository statistiekRepository;

    @InjectMocks
    private StatistiekService sut;


    // 1. TEST VOOR getGebruikerDashboard

    @Test
    void getGebruikerDashboard_GeeftStats() {
        // ARRANGE
        int gebruikerId = 1;

        // stel repository mocks in
        when(statistiekRepository.getTotaalGeleend(gebruikerId)).thenReturn(5);
        when(statistiekRepository.getPopulairsteGenre(gebruikerId)).thenReturn("Fantasy");

        // ACT
        GebruikerStats stats = sut.getGebruikerBoeken(gebruikerId);

        // ASSERT
        // controleer of totaal geleend klopt
        assertEquals(5, stats.getTotaalGeleend());

        // controleer of populairste genre klopt
        assertEquals("Fantasy", stats.getPopulairsteGenre());

        // verify repository calls
        verify(statistiekRepository).getTotaalGeleend(gebruikerId);
        verify(statistiekRepository).getPopulairsteGenre(gebruikerId);
    }


    // 2. TEST VOOR actualiseerBoekRating

    @Test
    void actualiseerBoekRating_UpdateGemiddeldeWordtUitgevoerd() {
        // ARRANGE
        int leningId = 10;
        int boekId = 3;
        Double gemiddelde = 4.2;

        // mocks instellen
        when(statistiekRepository.getBoekIdBijLening(leningId)).thenReturn(boekId);
        when(statistiekRepository.berekenGemiddeldeVoorBoek(boekId)).thenReturn(gemiddelde);

        // ACT
        sut.actualiseerBoekRating(leningId);

        // ASSERT
        // controleer of update is aangeroepen met juiste waarden
        verify(statistiekRepository).updateBoekGemiddelde(boekId, gemiddelde);
    }


    // 3. actualiseerBoekRating: gemiddeld is null → geen update

    @Test
    void actualiseerBoekRating_GemiddeldeNull_UpdateNietUitgevoerd() {
        // ARRANGE
        int leningId = 10;
        int boekId = 3;

        when(statistiekRepository.getBoekIdBijLening(leningId)).thenReturn(boekId);
        when(statistiekRepository.berekenGemiddeldeVoorBoek(boekId)).thenReturn(null);

        // ACT
        sut.actualiseerBoekRating(leningId);

        // ASSERT
        // update mag niet uitgevoerd worden
        verify(statistiekRepository, never()).updateBoekGemiddelde(anyInt(), any());
    }
}