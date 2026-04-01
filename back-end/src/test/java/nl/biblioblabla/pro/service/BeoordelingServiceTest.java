package nl.biblioblabla.pro.service;

import nl.biblioblabla.pro.repository.BeoordelingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

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
    void verwerkBeoordeling_MetRatingHogerDanVijf_GooitBadRequest() {
        // ARRANGE
        // We bereiden de testdata voor (expected)
        int leningId = 9;
        int gebruikerId = 6;
        int teHogeRating = 7;
        String expectedMessage = "Rating moet tussen 1 en 5 liggen.";

        //  ACT & ASSERT
        ResponseStatusException actual = assertThrows(ResponseStatusException.class, () -> {
            sut.verwerkBeoordeling(leningId, gebruikerId, teHogeRating);
        });

        // We controleren of de 'actual' waarden overeenkomen met de 'expected' waarden
        assertEquals(400, actual.getStatusCode().value());
        assertEquals(expectedMessage, actual.getReason());

        // Monitoring van de kernlogica: we bewijzen dat de repository NOOIT wordt aangeroepen bij een foutieve rating
        verifyNoInteractions(beoordelingRepository);
    }
}