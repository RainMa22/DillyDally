package me.rainma22.dillydally.exceptions;

/**
 * DillyDallyException
 */
public class DillyDallyException extends Exception {

    public DillyDallyException() {
    }

    public DillyDallyException(String message) {
        super(message);
    }

    public DillyDallyException(Throwable cause) {
        super(cause);
    }

    public DillyDallyException(String message, Throwable cause) {
        super(message, cause);
    }

}
