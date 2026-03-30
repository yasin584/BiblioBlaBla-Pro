package nl.biblioblabla.pro.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lening {

    private Integer id;
    private int gebruikerId;
    private int boekId;
    private LocalDate uitleendatum;
    private LocalDate inleverdatum;
    private Integer beoordeling;
    private boolean isIngeleverd;


    private String titel;
    private String auteurNaam;
    private String genre;
    private Double avgRating;
}