package nl.biblioblabla.pro.controller;

import nl.biblioblabla.pro.model.Lening;
import nl.biblioblabla.pro.model.User;
import nl.biblioblabla.pro.repository.LeningenRepository;
import nl.biblioblabla.pro.repository.UserRepository;
import nl.biblioblabla.pro.service.BeoordelingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeningenControllerTest {

    @Mock
    private LeningenRepository leningenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BeoordelingService beoordelingService;

    @InjectMocks
    private LeningenController sut;


    // 1. LENINGEN OPHALEN MET FILTERS

    @Test
    void getMijnLeningen_MetFilters_ReturnsLeningen() {
        // ARRANGE
        //ingelogde gebruiker
        Principal principal = () -> "test@example.com";

        // fake user
        User user = new User();
        user.setId(1);

        // fake lening
        Lening lening = new Lening();
        lening.setId(100);
        lening.setTitel("Harry Potter");

        // mocks instellen
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(leningenRepository.searchLeningen(
                eq(1),
                eq("Harry"),
                eq("Fantasy"),
                eq(LocalDate.of(2026, 1, 1)),
                eq(LocalDate.of(2026, 2, 1))
        )).thenReturn(List.of(lening));

        // ACT
        List<Lening> actual = sut.getMijnLeningen(
                principal,
                "Harry",
                "Fantasy",
                "2026-01-01",
                "2026-02-01"
        );

        // ASSERT
        // controleer of we 1 resultaat krijgen
        assertEquals(1, actual.size());

        // controleer of de titel klopt
        assertEquals("Harry Potter", actual.get(0).getTitel());

        // check of repository is aangeroepen
        verify(leningenRepository).searchLeningen(
                eq(1),
                eq("Harry"),
                eq("Fantasy"),
                eq(LocalDate.of(2026, 1, 1)),
                eq(LocalDate.of(2026, 2, 1))
        );
    }


    // 2. NIET INGELOGD

    @Test
    void getMijnLeningen_NietIngelogd_GooitUnauthorized() {
        // ARRANGE
        // geen ingelogde gebruiker
        Principal principal = null;

        // ACT & ASSERT
        // verwacht een 401 fout
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> sut.getMijnLeningen(principal, null, null, null, null)
        );

        // controleer statuscode en foutmelding
        assertEquals(401, exception.getStatusCode().value());
        assertEquals("Je moet ingelogd zijn.", exception.getReason());

        // er mogen geen database calls gedaan worden
        verifyNoInteractions(userRepository, leningenRepository);
    }


    // 3. GEBRUIKER NIET GEVONDEN

    @Test
    void getMijnLeningen_GebruikerNietGevonden_GooitNotFound() {
        // ARRANGE
        // ingelogde gebruiker met email die niet bestaat
        Principal principal = () -> "nietbestaan@example.com";

        // repository vindt geen user
        when(userRepository.findByEmail("nietbestaan@example.com")).thenReturn(null);

        // ACT & ASSERT
        // verwacht een 404 fout
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> sut.getMijnLeningen(principal, null, null, null, null)
        );

        // controleer statuscode en foutmelding
        assertEquals(404, exception.getStatusCode().value());
        assertEquals("Gebruiker niet gevonden.", exception.getReason());

        // userRepository moet wel aangeroepen zijn
        verify(userRepository).findByEmail("nietbestaan@example.com");

        // leningenRepository mag niet aangeroepen worden
        verifyNoInteractions(leningenRepository);
    }
}