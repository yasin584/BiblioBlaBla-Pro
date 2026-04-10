package nl.biblioblabla.pro.repository;


//import nl.biblioblabla.pro.exception.GenreNietGevondenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatistiekRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private StatistiekRepository sut;


    // 1. GEMIDDELDE BEREKENEN

    @Test
    void berekenGemiddeldeVoorBoek_GeeftGemiddelde() {
        // ARRANGE
        int boekId = 1;
        Double expected = 4.5;

        when(jdbcTemplate.queryForObject(anyString(), eq(Double.class), eq(boekId)))
                .thenReturn(expected);

        // ACT
        Double result = sut.berekenGemiddeldeVoorBoek(boekId);

        // ASSERT
        assertEquals(expected, result);

        // check of query is uitgevoerd
        verify(jdbcTemplate).queryForObject(anyString(), eq(Double.class), eq(boekId));
    }


    // 2. UPDATE BOEK GEMIDDELDE

    @Test
    void updateBoekGemiddelde_VoertUpdateUit() {
        // ARRANGE
        int boekId = 1;
        Double gemiddelde = 4.2;

        // ACT
        sut.updateBoekGemiddelde(boekId, gemiddelde);

        // ASSERT
        // check of update is aangeroepen
        verify(jdbcTemplate).update(anyString(), eq(gemiddelde), eq(boekId));
    }


    // 3. TOTAAL GELEEND

    @Test
    void getTotaalGeleend_GeeftAantal() {
        // ARRANGE
        int gebruikerId = 1;
        int expected = 5;

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(gebruikerId)))
                .thenReturn(expected);

        // ACT
        int result = sut.getTotaalGeleend(gebruikerId);

        // ASSERT
        assertEquals(expected, result);
    }


    // 4. POPULAIRSTE GENRE (SUCCES)

    @Test
    void getPopulairsteGenre_GeeftGenre() {
        // ARRANGE
        int gebruikerId = 1;
        String expected = "Fantasy";

        when(jdbcTemplate.queryForObject(anyString(), eq(String.class), eq(gebruikerId)))
                .thenReturn(expected);

        // ACT
        String result = sut.getPopulairsteGenre(gebruikerId);

        // ASSERT
        assertEquals(expected, result);
    }


    // 5. POPULAIRSTE GENRE (FOUT → DEFAULT)

//    @Test
//    void getPopulairsteGenre_BijFout_GooitEigenException() {
//        // ARRANGE
//        int gebruikerId = 1;
//        when(jdbcTemplate.queryForObject(anyString(), eq(String.class), eq(gebruikerId)))
//                .thenThrow(new RuntimeException("DB fout"));
//
//        // ACT & ASSERT
//        GenreNietGevondenException exception = assertThrows(
//                GenreNietGevondenException.class,
//                () -> sut.getPopulairsteGenre(gebruikerId)
//        );
//
//        // Update the expected string here
//        assertEquals("Geen leningen gevonden voor genre: Nog geen genres bekend voor gebruiker 1", exception.getMessage());
//    }


    // 6. BOEK ID BIJ LENING

    @Test
    void getBoekIdBijLening_GeeftBoekId() {
        // ARRANGE
        int leningId = 10;
        int expected = 3;

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(leningId)))
                .thenReturn(expected);

        // ACT
        int result = sut.getBoekIdBijLening(leningId);

        // ASSERT
        assertEquals(expected, result);
    }
}