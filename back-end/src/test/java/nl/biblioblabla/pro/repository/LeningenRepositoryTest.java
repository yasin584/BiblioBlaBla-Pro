package nl.biblioblabla.pro.repository;

//import nl.biblioblabla.pro.exception.GeenLeningenVoorGebruikerException;
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

//    @Test
//    void searchLeningen_GeenResultaten_GooitException() {
//        // ARRANGE
//        int gebruikerId = 1;
//        when(jdbcTemplate.query(anyString(), any(RowMapper.class), any(Object[].class)))
//                .thenReturn(List.of()); // Mock returns empty list
//
//        // ACT & ASSERT
//        assertThrows(GeenLeningenVoorGebruikerException.class, () -> {
//            sut.searchLeningen(gebruikerId, null, null, null, null);
//        });
//    }


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

//    @Test
//    void saveLening_GeldigeData_RoeptJdbcTemplateAan() {
//        // ARRANGE
//        int gebruikerId = 10;
//        int boekId = 50;
//        LocalDate inleverdatum = LocalDate.now().plusDays(21);
//
//        // De SQL-string die we verwachten (moet exact overeenkomen met de code)
//        String expectedSql = "INSERT INTO leningen (gebruiker_id, boek_id, uitleendatum, inleverdatum, is_ingeleverd) VALUES (?, ?, ?, ?, ?)";
//
//        // ACT
//        sut.saveLening(gebruikerId, boekId, inleverdatum);
//
//        // ASSERT
//        // We verifiëren of de update methode is aangeroepen met de juiste argumenten
//        verify(jdbcTemplate).update(
//                eq(expectedSql),
//                eq(gebruikerId),
//                eq(boekId),
//                any(LocalDate.class), // We gebruiken any() voor de 'uitleendatum' omdat dit LocalDate.now() is
//                eq(inleverdatum),
//                eq(false)
//        );
//    }
}