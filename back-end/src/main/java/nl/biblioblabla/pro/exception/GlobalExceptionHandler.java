package nl.biblioblabla.pro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAllUncaughtExceptions(Exception e) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Er is een onverwachte fout opgetreden. Probeer het later opnieuw."
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentialsException(InvalidCredentialsException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(OngeldigeRatingException.class)
    public ProblemDetail handleOngeldigeRating(OngeldigeRatingException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }


    @ExceptionHandler(GenreNietGevondenException.class)
    public ProblemDetail handleGenreNietGevonden(GenreNietGevondenException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(GeenLeningenVoorDatumException.class)
    public ProblemDetail handleGeenLeningenVoorDatum(GeenLeningenVoorDatumException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(GeenLeningenVoorGebruikerException.class)
    public ProblemDetail handleGeenLeningenVoorGebruiker(GeenLeningenVoorGebruikerException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(LeningenRepositoryException.class)
    public ProblemDetail handleLeningenRepositoryException(LeningenRepositoryException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}