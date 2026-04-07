package nl.biblioblabla.pro.service;

import nl.biblioblabla.pro.repository.BeoordelingRepository;
import nl.biblioblabla.pro.exception.OngeldigeRatingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class BeoordelingServiceTest {

    @Mock
    private BeoordelingRepository beoordelingRepository;

    @InjectMocks
    private BeoordelingService sut; // System Under Test

    @Test
    void verwerkBeoordeling_MetRatingHogerDanVijf_GooitException() {
        // ARRANGE
        int leningId = 9;
        int gebruikerId = 6;
        int teHogeRating = 7;
        String expectedMessage = "Rating moet tussen 1 en 5 liggen.";

        // ACT & ASSERT
        OngeldigeRatingException actual = assertThrows(
                OngeldigeRatingException.class,
                () -> sut.verwerkBeoordeling(leningId, gebruikerId, teHogeRating)
        );

        // ASSERT
        assertEquals(expectedMessage, actual.getMessage());

        // controleren dat repository niet wordt aangeroepen
        verifyNoInteractions(beoordelingRepository);
    }
}