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

        // Simuleer dat 1 rij wordt geüpdatet
        when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), anyInt()))
                .thenReturn(1);

        // ACT
        boolean actual = sut.voegBeoordelingToe(leningId, gebruikerId, rating);

        // ASSERT
        assertEquals(true, actual);

        // Controleer dat update is aangeroepen
        verify(jdbcTemplate).update(anyString(), anyInt(), anyInt(), anyInt());
    }

    @Test
    void voegBeoordelingToe_GeenRijenAangepast_ReturnsFalse() {
        // ARRANGE
        int leningId = 1;
        int gebruikerId = 2;
        int rating = 3;

        // Simuleer dat er geen rijen zijn geüpdatet
        when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), anyInt()))
                .thenReturn(0);

        // ACT
        boolean actual = sut.voegBeoordelingToe(leningId, gebruikerId, rating);

        // ASSERT
        assertEquals(false, actual);
    }

    @Test
    void voegBeoordelingToe_DatabaseFout_GooitException() {
        // ARRANGE
        int leningId = 1;
        int gebruikerId = 2;
        int rating = 4;

        // Simuleer database fout
        when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Database fout"));

        // ACT & ASSERT
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> sut.voegBeoordelingToe(leningId, gebruikerId, rating)
        );

        assertEquals("Database fout", exception.getMessage());
    }
}