package nl.biblioblabla.pro.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuteursRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AuteursRepository sut; // System Under Test

    // --- Tests voor getOrCreateAuteur ---

    @Test
    void getOrCreateAuteur_AuteurBestaatAl_GeeftIdTerugZonderInsert() {
        // ARRANGE
        String naam = "J.K. Rowling";
        Integer verwachteId = 123;

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(naam)))
                .thenReturn(verwachteId);

        // ACT
        Integer resultaat = sut.getOrCreateAuteur(naam);

        // ASSERT
        assertEquals(verwachteId, resultaat);
        // Controleer dat er geen update (insert) is uitgevoerd
        verify(jdbcTemplate, never()).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
    }

    @Test
    void getOrCreateAuteur_AuteurBestaatNiet_VoertInsertUitEnGeeftNieuweId() {
        // ARRANGE
        String naam = "Nieuwe Auteur";
        Integer nieuweId = 999;

        // 1. Simuleer dat de auteur niet gevonden wordt
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(naam)))
                .thenThrow(new EmptyResultDataAccessException(1));

        // 2. Simuleer de werking van de GeneratedKeyHolder
        // Omdat 'update' met een KeyHolder lastig te stubben is, gebruiken we een doAnswer
        doAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1);
            // Simuleer dat de database de ID 999 heeft gegenereerd
            kh.getKeyList().add(Map.of("id", nieuweId));
            return 1; // 1 rij aangepast
        }).when(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        // ACT
        Integer resultaat = sut.getOrCreateAuteur(naam);

        // ASSERT
        assertEquals(nieuweId, resultaat);
        verify(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
    }

    // --- Tests voor searchAuteurs ---

    @Test
    void searchAuteurs_RoeptJdbcTemplateAanMetWildcards() {
        // ARRANGE
        String zoekterm = "Row";
        List<String> verwachteLijst = List.of("J.K. Rowling");

        when(jdbcTemplate.queryForList(anyString(), eq(String.class), eq("%Row%")))
                .thenReturn(verwachteLijst);

        // ACT
        List<String> resultaat = sut.searchAuteurs(zoekterm);

        // ASSERT
        assertEquals(1, resultaat.size());
        assertEquals("J.K. Rowling", resultaat.get(0));
        verify(jdbcTemplate).queryForList(contains("LIKE ?"), eq(String.class), eq("%Row%"));
    }
}