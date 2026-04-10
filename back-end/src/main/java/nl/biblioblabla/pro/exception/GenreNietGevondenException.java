package nl.biblioblabla.pro.exception;

public class GenreNietGevondenException extends RuntimeException {
    public GenreNietGevondenException(String genre) {
        super("Geen leningen gevonden voor genre: " + genre);
    }
}
