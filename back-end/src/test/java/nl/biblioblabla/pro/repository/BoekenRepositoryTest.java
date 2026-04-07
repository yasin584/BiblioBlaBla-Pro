package nl.biblioblabla.pro.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoekenRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private BoekenRepository sut;

    // --- Tests voor getOrCreateBoek ---

    @Test
    void getOrCreateBoek_BestaandBoek_GeeftIdTerug() {
        // ARRANGE
        String titel = "Harry Potter";
        int auteurId = 1;
        Integer verwachteId = 101;

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(titel), eq(auteurId)))
                .thenReturn(verwachteId);

        // ACT
        Integer resultaat = sut.getOrCreateBoek(titel, "Fantasy", auteurId);

        // ASSERT
        assertEquals(verwachteId, resultaat);
        verify(jdbcTemplate, never()).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
    }

    @Test
    void getOrCreateBoek_NieuwBoek_VoertInsertUitEnGeeftId() {
        // ARRANGE
        String titel = "Nieuw Boek";
        int auteurId = 2;
        Integer nieuweId = 202;

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(titel), eq(auteurId)))
                .thenThrow(new EmptyResultDataAccessException(1));

        // Simuleer de database insert en KeyHolder
        doAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(Map.of("id", nieuweId));
            return 1;
        }).when(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        // ACT
        Integer resultaat = sut.getOrCreateBoek(titel, "Thriller", auteurId);

        // ASSERT
        assertEquals(nieuweId, resultaat);
        verify(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
    }

    // --- Tests voor searchTitels ---

    @Test
    void searchTitels_RoeptJdbcTemplateAanMetWildcards() {
        // ARRANGE
        String query = "Potter";
        List<String> verwachteTitels = List.of("Harry Potter en de Steen der Wijzen");

        when(jdbcTemplate.queryForList(anyString(), eq(String.class), eq("%Potter%")))
                .thenReturn(verwachteTitels);

        // ACT
        List<String> resultaat = sut.searchTitels(query);

        // ASSERT
        assertEquals(1, resultaat.size());
        assertTrue(resultaat.contains("Harry Potter en de Steen der Wijzen"));
    }

    // --- Tests voor findGenreByTitel ---

    @Test
    void findGenreByTitel_BoekBestaat_GeeftGenreTerug() {
        // ARRANGE
        String titel = "Harry Potter";
        String verwachtGenre = "Fantasy";

        when(jdbcTemplate.queryForObject(anyString(), eq(String.class), eq(titel)))
                .thenReturn(verwachtGenre);

        // ACT
        String resultaat = sut.findGenreByTitel(titel);

        // ASSERT
        assertEquals(verwachtGenre, resultaat);
    }

    @Test
    void findGenreByTitel_BoekBestaatNiet_GeeftNullTerug() {
        // ARRANGE
        String titel = "Onbekend Boek";

        when(jdbcTemplate.queryForObject(anyString(), eq(String.class), eq(titel)))
                .thenThrow(new EmptyResultDataAccessException(1));

        // ACT
        String resultaat = sut.findGenreByTitel(titel);

        // ASSERT
        assertNull(resultaat, "De methode moet null teruggeven als de database geen resultaat vindt.");
    }
}