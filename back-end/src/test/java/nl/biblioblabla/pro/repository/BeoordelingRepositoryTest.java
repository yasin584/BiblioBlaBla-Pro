package nl.biblioblabla.pro.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BeoordelingRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private BeoordelingRepository sut; // System Under Test

    @Test
    void voegBeoordelingToe_GeldigeInput_UpdateGelukt_ReturnsTrue() {
        // ARRANGE
        int leningId = 1;
        int gebruikerId = 2;
        int rating = 5;

        // De exacte SQL query die in de repository staat
        String expectedSql = "UPDATE leningen SET beoordeling = ? WHERE id = ? AND gebruiker_id = ?";

        // Gebruik eq() voor exacte waarden in plaats van any()
        when(jdbcTemplate.update(eq(expectedSql), eq(rating), eq(leningId), eq(gebruikerId)))
                .thenReturn(1);

        // ACT
        boolean actual = sut.voegBeoordelingToe(leningId, gebruikerId, rating);

        // ASSERT
        assertEquals(true, actual);

        // Controleer met de exacte parameters
        verify(jdbcTemplate).update(eq(expectedSql), eq(rating), eq(leningId), eq(gebruikerId));
    }

    @Test
    void voegBeoordelingToe_DatabaseFout_GooitException() {
        // ARRANGE
        int leningId = 1;
        int gebruikerId = 2;
        int rating = 4;
        String expectedSql = "UPDATE leningen SET beoordeling = ? WHERE id = ? AND gebruiker_id = ?";

        when(jdbcTemplate.update(eq(expectedSql), eq(rating), eq(leningId), eq(gebruikerId)))
                .thenThrow(new RuntimeException("Database fout"));

        // ACT & ASSERT
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> sut.voegBeoordelingToe(leningId, gebruikerId, rating)
        );

        assertEquals("Database fout", exception.getMessage());
    }
}