package nl.biblioblabla.pro.exception;

// simpele unchecked exception
public class GenreNietBekendException extends RuntimeException {
    public GenreNietBekendException(String message) {
        super(message);
    }
}