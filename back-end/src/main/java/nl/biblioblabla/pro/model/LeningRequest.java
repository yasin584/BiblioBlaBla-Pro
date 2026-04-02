package nl.biblioblabla.pro.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LeningRequest {
    private String titel;
    private String auteur;
    private String genre;
    private LocalDate inleverdatum;

    public LeningRequest() {
    }

    public LeningRequest(String titel, String auteur, String genre) {
        this.titel = titel;
        this.auteur = auteur;
        this.genre = genre;
        this.inleverdatum = LocalDate.now();
    }
}

