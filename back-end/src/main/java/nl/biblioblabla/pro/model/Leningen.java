package nl.biblioblabla.pro.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Leningen {

    private int id;
    private int gebruikerId;
    private int boekId;
    private LocalDate uitleendatum;
    private LocalDate inleverdatum;
    private int beoordeling;
    private String status;

    public Leningen() {
    }

}
