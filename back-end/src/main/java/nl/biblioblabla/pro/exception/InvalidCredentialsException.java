package nl.biblioblabla.pro.exception;

import lombok.Getter;

@Getter

public class InvalidCredentialsException extends RuntimeException{

    private String message;

    public InvalidCredentialsException(String message) {
        super(message);
        this.message = message;
    }
}
