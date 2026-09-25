package me.rainma22.dillydally.exceptions;

public class UnsupportedCompletorTypeException extends DillyDallyException {

    public UnsupportedCompletorTypeException() {
    }

    public UnsupportedCompletorTypeException(String message) {
        super(message);
    }

    public UnsupportedCompletorTypeException(Throwable cause) {
        super(cause);
    }

    public UnsupportedCompletorTypeException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
