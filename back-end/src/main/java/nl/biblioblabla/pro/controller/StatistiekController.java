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
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatistiekController {
    private final StatistiekService statistiekService;
    private final UserRepository userRepository;

    @GetMapping("/mij")
    public GebruikerStats getMijnStats(Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        return statistiekService.getGebruikerDashboard(user.getId());
    }
}
