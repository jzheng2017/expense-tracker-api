package webservice.exceptions;

public class BadParameterException extends RuntimeException {
    public BadParameterException(String message) {
        super(message);
    }
}
