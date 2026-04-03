package nl.biblioblabla.pro.controller;

import nl.biblioblabla.pro.model.GebruikerStats;
import nl.biblioblabla.pro.model.User;
import nl.biblioblabla.pro.repository.UserRepository;
import nl.biblioblabla.pro.service.StatistiekService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatistiekControllerTest {

    @Mock
    private StatistiekService statistiekService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Principal principal;

    @InjectMocks
    private StatistiekController sut;

    // 1. NORMALE FLOW

    @Test
    void getMijnStats_GeeftResultaat() {
        // ARRANGE
        // we simuleren een ingelogde gebruiker
        String email = "test@test.nl";
        when(principal.getName()).thenReturn(email);

        // we maken een fake user
        User user = new User();
        user.setId(1);
        user.setEmail(email);

        // fake resultaat
        GebruikerStats stats = new GebruikerStats();

        // mocks instellen
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(statistiekService.getGebruikerDashboard(1)).thenReturn(stats);

        // ACT
        GebruikerStats result = sut.getMijnStats(principal);

        // ASSERT
        assertNotNull(result); // resultaat mag niet null zijn
        assertEquals(stats, result); // moet gelijk zijn aan verwacht

        // controleren of methods zijn aangeroepen
        verify(userRepository).findByEmail(email);
        verify(statistiekService).getGebruikerDashboard(1);
    }


    // 2. GEEN USER GEVONDEN

    @Test
    void getMijnStats_GeenUser_GooitException() {
        // ARRANGE
        when(principal.getName()).thenReturn("test@test.nl");

        // repository geeft geen user terug
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        // ACT & ASSERT
        // omdat user null is, ontstaat er een fout (NullPointerException)
        assertThrows(NullPointerException.class,
                () -> sut.getMijnStats(principal));
    }
}