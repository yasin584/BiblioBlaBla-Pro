package nl.biblioblabla.pro.controller;

import nl.biblioblabla.pro.model.Lening;
import nl.biblioblabla.pro.repository.LeningenRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/leningen") // Toegevoegd /api voor best-practice
public class LeningenController {

    private final LeningenRepository leningenRepository;

    public LeningenController(LeningenRepository leningenRepository) {
        this.leningenRepository = leningenRepository;
    }

    @GetMapping("/gebruiker/{id}")
    public List<Lening> getByGebruiker(@PathVariable int id) {
        return leningenRepository.findByGebruikerId(id);
    }

    @GetMapping("/filter/zoek")
    public List<Lening> search(@RequestParam String q) {
        return leningenRepository.filterOpTitelOfAuteur(q);
    }

    @GetMapping("/filter/genre")
    public List<Lening> getByGenre(@RequestParam String g) {
        return leningenRepository.filterOpGenre(g);
    }

    @GetMapping("/filter/datum")
    public List<Lening> getByDatum(@RequestParam String start, @RequestParam String eind) {
        return leningenRepository.filterOpDatumRange(LocalDate.parse(start), LocalDate.parse(eind));
    }
}