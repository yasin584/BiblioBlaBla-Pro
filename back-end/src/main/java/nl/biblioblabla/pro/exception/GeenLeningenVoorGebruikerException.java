package nl.biblioblabla.pro.exception;

public class GeenLeningenVoorGebruikerException extends RuntimeException {
    public GeenLeningenVoorGebruikerException(int gebruikerId) {
        super("Geen leningen gevonden voor gebruiker met id: " + gebruikerId);
    }
}