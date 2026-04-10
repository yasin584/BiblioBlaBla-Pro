package nl.biblioblabla.pro.exception;

import java.time.LocalDate;

public class GeenLeningenVoorDatumException extends RuntimeException {
    public GeenLeningenVoorDatumException(LocalDate start, LocalDate eind) {
        super("Geen leningen gevonden tussen " + start + " en " + eind);
    }
}
