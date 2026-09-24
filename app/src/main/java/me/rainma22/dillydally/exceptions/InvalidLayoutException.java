package me.rainma22.dillydally.exceptions;

public class InvalidLayoutException extends DillyDallyException {

    public InvalidLayoutException() {
    }

    public InvalidLayoutException(String message) {
        super(message);
    }

    public InvalidLayoutException(Throwable cause) {
        super(cause);
    }

    public InvalidLayoutException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
