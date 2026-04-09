package nl.biblioblabla.pro.controller;

import lombok.RequiredArgsConstructor;
import nl.biblioblabla.pro.model.GebruikerStats;
import nl.biblioblabla.pro.model.User;
import nl.biblioblabla.pro.repository.UserRepository;
import nl.biblioblabla.pro.service.StatistiekService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/leningen/stats")
@RequiredArgsConstructor
public class StatistiekController {

    private final StatistiekService statistiekService;
    private final UserRepository userRepository;

    @GetMapping("/mij")  // endpoint: GET /leningen/stats/mij
    public GebruikerStats getMijnStats(Principal principal) {

        //Haal de email van de ingelogde gebruiker op via principal
        String email = principal.getName();

        //Zoekt de gebruiker in de database
        User user = userRepository.findByEmail(email);


        //Vraag de statistieken op via de service laag
        GebruikerStats stats = statistiekService.getGebruikerDashboard(user.getId());

        //Geef de statistieken terug aan de client
        return stats;
    }
}