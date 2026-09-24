package me.rainma22.dillydally.exceptions;

public class InvalidNamespaceException extends DillyDallyException{

    public InvalidNamespaceException() {
    }

    public InvalidNamespaceException(String message) {
        super(message);
    }

    public InvalidNamespaceException(Throwable cause) {
        super(cause);
    }

    public InvalidNamespaceException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
