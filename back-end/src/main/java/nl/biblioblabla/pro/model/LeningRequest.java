package nl.biblioblabla.pro.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeningRequest {
    private String titel;
    private String auteur;
    private String genre;
    private LocalDate inleverdatum;
}

