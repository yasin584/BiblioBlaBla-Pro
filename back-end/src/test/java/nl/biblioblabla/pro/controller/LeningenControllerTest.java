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


    // leningen met filters
    @Test
    void getMijnLeningen_MetFilters_ReturnsLeningen() {
        // ARRANGE
        Principal principal = () -> "test@example.com";
        User user = new User();
        user.setId(1);

        Lening lening = new Lening();
        lening.setId(100);
        lening.setTitel("Harry Potter");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(leningenRepository.searchLeningen(eq(1), eq("Harry"), eq("Fantasy"),
                eq(LocalDate.of(2026, 1, 1)), eq(LocalDate.of(2026, 2, 1))))
                .thenReturn(List.of(lening));

        // ACT
        List<Lening> actual = sut.getMijnLeningen(principal, "Harry", "Fantasy", "2026-01-01", "2026-02-01");

        // ASSERT
        assertEquals(1, actual.size());
        assertEquals("Harry Potter", actual.get(0).getTitel());
        verify(leningenRepository).searchLeningen(eq(1), eq("Harry"), eq("Fantasy"),
                eq(LocalDate.of(2026, 1, 1)), eq(LocalDate.of(2026, 2, 1)));
    }


    // 2. Gebruiker niet ingelogd
    @Test
    void getMijnLeningen_NietIngelogd_GooitUnauthorized() {
        // ARRANGE
        Principal principal = null;

        // ACT & ASSERT
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> sut.getMijnLeningen(principal, null, null, null, null));

        assertEquals(401, exception.getStatusCode().value());
        assertEquals("Je moet ingelogd zijn.", exception.getReason());
        verifyNoInteractions(userRepository, leningenRepository);
    }

    // 3. Gebruiker niet gevonden
    @Test
    void getMijnLeningen_GebruikerNietGevonden_GooitNotFound() {
        // ARRANGE
        Principal principal = () -> "nietbestaan@example.com";
        when(userRepository.findByEmail("nietbestaan@example.com")).thenReturn(null);

        // ACT & ASSERT
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> sut.getMijnLeningen(principal, null, null, null, null));

        assertEquals(404, exception.getStatusCode().value());
        assertEquals("Gebruiker niet gevonden.", exception.getReason());
        verify(userRepository).findByEmail("nietbestaan@example.com");
        verifyNoInteractions(leningenRepository);
    }
}