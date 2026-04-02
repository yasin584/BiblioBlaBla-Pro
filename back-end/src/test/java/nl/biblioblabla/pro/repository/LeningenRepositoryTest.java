package nl.biblioblabla.pro.repository;

import nl.biblioblabla.pro.model.Lening;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LeningenRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private LeningenRepository sut;

    // zoek functie

    @Test
    void searchLeningen_MetFilters_GeeftResultaat() {
        // ARRANGE
        int gebruikerId = 1;
        String titel = "Harry";
        String genre = "Fantasy";
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate eind = LocalDate.of(2026, 2, 1);

        Lening lening = new Lening();
        lening.setId(1);
        lening.setTitel("Harry Potter");

        List<Lening> expected = List.of(lening);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenReturn(expected);

        // ACT
        List<Lening> actual = sut.searchLeningen(gebruikerId, titel, genre, start, eind);

        // ASSERT
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getTitel(), actual.get(0).getTitel());

        verify(jdbcTemplate).query(anyString(), any(RowMapper.class), any(Object[].class));
    }


    // 2. GEEN RESULTATEN

    @Test
    void searchLeningen_GeenResultaten_ReturnsLegeLijst() {
        // ARRANGE
        List<Lening> expected = List.of();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenReturn(expected);

        // ACT
        List<Lening> actual = sut.searchLeningen(1, null, null, null, null);

        // ASSERT
        assertEquals(expected.size(), actual.size());
    }


    // DATABASE FOUT

    @Test
    void searchLeningen_DatabaseFout_GooitException() {
        // ARRANGE
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenThrow(new RuntimeException("Database fout"));

        // ACT & ASSERT
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> sut.searchLeningen(1, null, null, null, null)
        );

        assertEquals("Database fout", exception.getMessage());
    }
}